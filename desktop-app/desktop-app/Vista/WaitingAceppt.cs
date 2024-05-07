using desktop_app.Controlador;
using desktop_app.Modelo;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace desktop_app.Vista {
    public partial class WaitingAccept : Form {
        private Host h;
        private Thread hiloAccept;
        private bool flagStop;
        private string folder;
        private string ip;

        public WaitingAccept(string ip, string folder) {
            InitializeComponent();
            this.h = new Host(ip, 7777);
            flagStop = false;
            this.folder = folder;
            this.ip = ip;
        }

        private void WaitingAceppt_Shown(object sender, EventArgs e) {
            hiloAccept = new Thread(Host_Accept);
            hiloAccept.Start();
        }

        private void Host_Accept(object obj) {
            ReceivingProcess rp = null;

            while (!flagStop) {
                if(this.h.Accept()) {
                    byte[] opc = new byte[1];
                    this.h.Recibir(opc);
                    if (opc[0] == HostMessage.PROCEED[0]) {
                        rp = new ReceivingProcess(h, folder);
                        break;
                    }else { this.h.Cerrar(); }
                }
            }

            try {
                this.Invoke((Action)(() => {
                    rp?.ShowDialog();
                    this.Close();
                }));
            } catch { }
        }

        private void btnCancelar_Click(object sender, EventArgs e) {
            flagStop = true;
            this.h.CerrarServer();
            this.h.Cerrar();
        }

        private void WaitingAceppt_FormClosing(object sender, FormClosingEventArgs e) {
            flagStop = true;
            this.h.CerrarServer();
            this.h.Cerrar();
        }
    }
}
