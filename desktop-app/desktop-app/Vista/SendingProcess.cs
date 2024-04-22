using desktop_app.Controlador;
using desktop_app.Modelo;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Security.Policy;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace desktop_app.Vista {
    public partial class SendingProcess : Form {
        private Host h;
        private List<MyFile> files;
        private bool flagStop;
        private bool flagError;
        Thread hiloSend;

        public SendingProcess(Host h, List<MyFile> files) {
            InitializeComponent();
            this.h = h;
            this.files = files;
            flagStop = false;
            flagError = false;
        }

        private void SendProcess() {
            FileStream fs;
            byte[] buffer = new byte[65535];
            double proBar = 0;

            byte[] fileCount = BitConverter.GetBytes(files.Count);

            if (this.h.Enviar(fileCount)) {
                foreach (MyFile file in files) {
                    if (flagStop) break;

                    string json = JsonConvert.SerializeObject(file);
                    byte[] _json;
                    byte[] _jsonL;

                    try {
                        _json = Encoding.UTF8.GetBytes(json);
                        _jsonL = BitConverter.GetBytes(_json.Length);
                    } catch {
                        flagError = true;
                        break;
                    }

                    if (this.h.Enviar(_jsonL) && this.h.Enviar(_json)) {
                        byte[] opc = new byte[1];

                        if(this.h.Recibir(opc) > 0) {
                            if (opc[0] == 1) {
                                int bytesRead;

                                try {
                                    using (fs = new FileStream(file.Path, FileMode.Open, FileAccess.Read)) {
                                        while ((bytesRead = fs.Read(buffer, 0, buffer.Length)) > 0 && !flagStop) {
                                            if (this.h.Enviar(buffer, 0, bytesRead)) {
                                                proBar += ((double)bytesRead * (100 / files.Count) / file.Size);

                                                try {
                                                    this.Invoke((Action)(() => { this.pBar.Value = (int)Math.Round(proBar); }));
                                                } catch { }
                                            } else {
                                                flagError = true;
                                                break;
                                            }
                                        }
                                    }
                                } catch { flagError = true; }
                            }
                        } else {
                            flagError = true;
                        }
                    } else {
                        flagError = true;
                    }

                    if (flagError) break;
                }
            }else { flagError = true; }

            if(flagError) MessageBox.Show("Hubo un error en el proceso de envío.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            if (!flagStop && !flagError) { MessageBox.Show("Enviado con éxito", "Información", MessageBoxButtons.OK, MessageBoxIcon.Information); }

            flagStop = true;

            try {
                this.Invoke((Action)(() => { this.Close(); }));
            } catch { }

        }

        private void LoadProcess_Shown(object sender, EventArgs e) {
            hiloSend = new Thread(SendProcess);
            hiloSend.Start();
        }

        private void btnCancelar_Click(object sender, EventArgs e) {
            this.flagStop = true;
            this.h.Cerrar();
        }

        private void SendingProcess_FormClosing(object sender, FormClosingEventArgs e) {
            if(!flagStop) this.flagStop = true;
            this.h.Cerrar();
        }
    }
}
