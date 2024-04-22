using desktop_app.Modelo;
using desktop_app.Properties;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.NetworkInformation;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace desktop_app.Vista {
    public partial class Home : Form {
        private List<MyFile> lstFiles;

        public Home() {
            InitializeComponent();
            lstFiles = new List<MyFile>();
        }

        private void btnEnviar_Click(object sender, EventArgs e) {
            FileInfo fi;
            OpenFileDialog fileDialog = new OpenFileDialog();
            fileDialog.InitialDirectory = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile);
            fileDialog.Multiselect = true;
            DialogResult dr = fileDialog.ShowDialog();

            if (dr == DialogResult.OK) {
                string[] files = fileDialog.FileNames;
                string[] names = new string[files.Length];

                for(int i=0; i<files.Length; i++) {
                    fi = new FileInfo(files[i]);
                    string[] _file = files[i].Split('\\');
                    lstFiles.Add(new MyFile(files[i], fi.Length, _file[_file.Length - 1]));
                }

                InterfaceSelection interS = new InterfaceSelection();

                NetworkInterface[] interfaces = NetworkInterface.GetAllNetworkInterfaces();

                foreach (NetworkInterface intf in interfaces) {
                    if (intf.OperationalStatus == OperationalStatus.Up &&
                        (intf.NetworkInterfaceType == NetworkInterfaceType.Ethernet ||
                        intf.NetworkInterfaceType == NetworkInterfaceType.Wireless80211)) {
                        IPInterfaceProperties properties = intf.GetIPProperties();

                        foreach (UnicastIPAddressInformation ip in properties.UnicastAddresses) {
                            if (ip.Address.AddressFamily == System.Net.Sockets.AddressFamily.InterNetwork) {
                                SelectionControl sc = new SelectionControl();
                                sc.SetName(intf.Name);
                                sc.SetImage(Resources.adapter);
                                sc.ControlClick += delegate {
                                    ScannerHost sh = new ScannerHost(ip.Address.ToString(), lstFiles);
                                    sh.ShowDialog();
                                    interS.Close();
                                };

                                interS.AddSelectionControl(sc);
                                break;
                            }
                        }
                    }
                }

                interS.ShowDialog();
            }
        }

        private void btnRecibir_Click(object sender, EventArgs e) {
            FolderBrowserDialog fbd = new FolderBrowserDialog();
            fbd.Description = "Escoga donde guardar los archivos.";
            DialogResult dr = fbd.ShowDialog();

            if (dr == DialogResult.OK) {
                string folder = fbd.SelectedPath;

                InterfaceSelection interS = new InterfaceSelection();
                NetworkInterface[] interfaces = NetworkInterface.GetAllNetworkInterfaces();

                foreach (NetworkInterface intf in interfaces) {
                    if (intf.OperationalStatus == OperationalStatus.Up &&
                        (intf.NetworkInterfaceType == NetworkInterfaceType.Ethernet ||
                        intf.NetworkInterfaceType == NetworkInterfaceType.Wireless80211)) {
                        IPInterfaceProperties properties = intf.GetIPProperties();

                        foreach (UnicastIPAddressInformation ip in properties.UnicastAddresses) {
                            if (ip.Address.AddressFamily == System.Net.Sockets.AddressFamily.InterNetwork) {
                                SelectionControl sc = new SelectionControl();
                                sc.SetName(intf.Name);
                                sc.SetImage(Resources.adapter);
                                sc.ControlClick += delegate {
                                    WaitingAceppt wa = new WaitingAceppt(ip.Address.ToString(), folder);
                                    wa.ShowDialog();
                                    interS.Close();
                                };

                                interS.AddSelectionControl(sc);
                                break;
                            }
                        }
                    }
                }

                interS.ShowDialog();
            }
        }
    }
}
