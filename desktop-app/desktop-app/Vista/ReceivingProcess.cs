using desktop_app.Controlador;
using desktop_app.Modelo;
using Newtonsoft.Json;
using System;
using System.IO;
using System.Text;
using System.Threading;
using System.Windows.Forms;

namespace desktop_app.Vista {
    public partial class ReceivingProcess : Form {
        private Host h;
        private Thread hiloReceive;
        DialogResult dr;
        private string folder;
        private bool flagStop;

        public ReceivingProcess(Host h, string folder) {
            InitializeComponent();
            this.folder = folder;
            this.h = h;
            flagStop = false;
        }

        private void LoadProcess_Shown(object sender, EventArgs e) {
            hiloReceive = new Thread(Receive_Process);
            hiloReceive.Start();
        }

        private void Receive_Process() {
            FileStream fs = null;
            byte[] fileCount_ = new byte[4];
            int result;
            double proBar = 0;
            int filesReceived = 0;

            if((result = this.h.Recibir(fileCount_)) > 0) {
                int fileCount = 0;
                try {
                    fileCount = BitConverter.ToInt32(fileCount_, 0);
                }catch { result = -2; }

                for(int i=0; i<fileCount && !flagStop; i++) {
                    byte[] jsonL_ = new byte[4];

                    if ((result = this.h.Recibir(jsonL_)) > 0) {
                        int jsonL;
                        byte[] json_;
                        try {
                            jsonL = BitConverter.ToInt32(jsonL_, 0);
                        }catch { result = -2; break; }

                        json_ = new byte[jsonL];

                        if((result = this.h.Recibir(json_)) > 0) {
                            string json;
                            try {
                                json = Encoding.UTF8.GetString(json_);
                            }catch { result = -2; break; }

                            MyFile f = JsonConvert.DeserializeObject<MyFile>(json);

                            if(f != null) {

                                if (File.Exists(folder + "\\" + f.Name)) {
                                    try {
                                        this.Invoke((Action)(() => {
                                            dr = MessageBox.Show("¿Desea sobreescribir el archivo " + f.Name + "?", "Pregunta", MessageBoxButtons.YesNo, MessageBoxIcon.Question);
                                        }));
                                    } catch { }

                                    if (dr == DialogResult.No) continue;
                                }

                                if (this.h.Enviar(HostMessage.PROCEED)) {
                                    fs = File.Create(folder + "\\" + f.Name);

                                    byte[] buffer = new byte[65535];
                                    int flagTamanoFile = 0;
                                    int bytesRead = 0;
                                    bool flagBuffer = false;

                                    while (!(flagTamanoFile == f.Size) && (bytesRead = this.h.Recibir(buffer)) > 0 && !flagStop) {
                                        fs.Write(buffer, 0, bytesRead);
                                        flagTamanoFile += bytesRead;

                                        proBar += ((double)bytesRead * (100 / fileCount) / f.Size);

                                        try {
                                            this.Invoke(new Action(() => { pBar.Value = (int)Math.Round(proBar); }));
                                        } catch { }

                                        if (flagBuffer) {
                                            buffer = new byte[65535];
                                            flagBuffer = false;
                                        }

                                        if (!(flagTamanoFile == f.Size) && (flagTamanoFile + 65535) > f.Size) {
                                            buffer = new byte[f.Size - flagTamanoFile];
                                            flagBuffer = true;
                                        }
                                    }

                                    if (flagTamanoFile == f.Size) filesReceived++;
                                    fs.Close();
                                    if (bytesRead == -1) {
                                        result = -1;
                                        File.Delete(folder + "\\" + f.Name);
                                    }
                                } else { result = -1; break; }
                            } else { result = -2; break; }
                        }else { break; }
                    }else { break; }
                }
            }

            if(result == -1) MessageBox.Show("Error de conexión", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            if(result == -2) MessageBox.Show("Error interno.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            if(!flagStop && result >= 0 && filesReceived>0) MessageBox.Show("Recibido con éxito", "Información", MessageBoxButtons.OK, MessageBoxIcon.Information);

            try {
                this.Invoke(new Action(() => { this.Close(); }));
            } catch { }
        }
        private void btnCancelar_Click(object sender, EventArgs e) {
            flagStop = true;
            this.h.CerrarServer();
        }

        private void ReceivingProcess_FormClosed(object sender, FormClosedEventArgs e) {
            flagStop = true;
            this.h.CerrarServer();
        }
    }
}
