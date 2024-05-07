using desktop_app.Controlador;
using desktop_app.Modelo;
using desktop_app.Properties;
using System;
using System.Collections.Generic;
using System.Net;
using System.Runtime.Remoting.Channels;
using System.Threading;
using System.Windows.Forms;

namespace desktop_app.Vista {
    public partial class ScannerHost : Form {
        private Thread hiloBusqueda;
        private bool flagStop;
        private int contSelXFila;
        private string ipAddress;
        private List<Host> lstHosts;
        private List<MyFile> lstFiles;

        public ScannerHost(string ipAddress, List<MyFile> files) {
            InitializeComponent();
            flagStop = false;
            contSelXFila = 0;
            this.ipAddress = ipAddress;
            this.lstHosts = new List<Host>();
            lstFiles = files;
        }

        private void BuscarHots() {
            string[] _ip = ipAddress.Split('.');

            for (int i = 2; i<255 && !flagStop; i++) {
                string ip = _ip[0] + "." + _ip[1] + "." + _ip[2] + "." + Convert.ToString(i);

                Host h = new Host();
                if (h.Conectar(ip, 7777, 100)) {
                    h.Enviar(HostMessage.WAIT);
                    lstHosts.Add(h);

                    SelectionControl sc = new SelectionControl();
                    string hostname = null;

                    try { hostname = Dns.GetHostEntry(ip).HostName; } catch { }

                    sc.SetName((hostname != null) ? hostname : ip);
                    sc.SetImage(Resources.device);
                    sc.ControlClick += delegate {
                        flagStop = true;
                        h.Cerrar();
                        h = new Host();
                        if(h.Conectar(ip, 7777, 500)) {
                            h.Enviar(HostMessage.PROCEED);
                            SendingProcess sp = new SendingProcess(h, lstFiles);
                            sp.ShowDialog();
                            this.Close();
                        } else {
                            MessageBox.Show("No se pudo conectar al host remoto.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        }
                        
                    };

                    try {
                        this.Invoke(new Action(() => { layoutHost.Controls.Add(sc); }));
                        contSelXFila++;
                        Application.DoEvents();

                        if (contSelXFila > 1) {
                            this.Invoke(new Action(() => {
                                layoutHost.RowCount++;
                                layoutHost.RowStyles.Add(new RowStyle(SizeType.AutoSize));
                            }));
                            contSelXFila = 0;
                        }
                    }catch { }
                }else {
                    h.Cerrar();
                }
            }

            try {
                this.Invoke(new Action(() => { btnBuscar.Text = "Buscar"; }));
            }catch { }
            
        }

        private void btnBuscar_Click(object sender, EventArgs e) {
            flagStop = false;
            layoutHost.Controls.Clear();
            layoutHost.RowCount = 1;
            CerrarConexionesHost();
            btnBuscar.Text = "Buscando...";
            hiloBusqueda = new Thread(BuscarHots);
            hiloBusqueda.Start();
        }

        private void ScannerHost_Shown(object sender, EventArgs e) {
            hiloBusqueda = new Thread(BuscarHots);
            hiloBusqueda.Start();
        }

        private void ScannerHost_FormClosing(object sender, FormClosingEventArgs e) {
            flagStop = true;
            CerrarConexionesHost();
        }

        private void CerrarConexionesHost() {
            foreach (Host h in lstHosts) {
                h.Cerrar();
            }
        }
    }
}
