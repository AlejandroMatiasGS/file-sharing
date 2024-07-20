using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace desktop_app.Controlador {
    public class Host {
        private Socket server;
        private Socket client;

        public bool Connected {
            get {
                return this.client.Poll(1000, SelectMode.SelectError);
            }
        }

        public Host() {
            try {
                client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            } catch (Exception ex) {
                Console.WriteLine(ex.ToString() + " " + ex.Message);
            }
        }

        public Host(string ip, int port) {
            server = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            server.Bind(new IPEndPoint(IPAddress.Parse(ip), port));
            server.Listen(1);
        }

        public void Cerrar() {
            try {
                this.client?.Close();
                this.client?.Dispose();
            } catch { }
        }

        public void CerrarServer() {
            try {
                this.server?.Close();
                this.server?.Dispose();
            } catch { }
        }

        public bool Accept() {
            try {
                if (this.server.Poll(0, SelectMode.SelectRead)) {
                    this.client = this.server.Accept();
                    this.client.ReceiveTimeout = 7000;
                    this.client.SendTimeout = 7000;
                    return true;
                } else return false;
            } catch { return false; }
        }

        public bool Conectar(String ip, int port, int connectTimeout) {
            try {
                IAsyncResult ar = this.client.BeginConnect(new IPEndPoint(IPAddress.Parse(ip), port), null, null);
                if (ar.AsyncWaitHandle.WaitOne(connectTimeout)) {
                    this.client.EndConnect(ar);
                    this.client.ReceiveTimeout = 7000;
                    this.client.SendTimeout = 7000;
                    return true;
                } else { return false; }
            } catch {
                return false;
            }
        }

        public bool Enviar(byte[] data) {
            try {
                this.client.Send(data, 0, data.Length, SocketFlags.None);
                return true;
            } catch (Exception e) { Console.WriteLine(e.Message); return false; }
        }

        public bool Enviar(byte[] data, int offset, int length) {
            try {
                this.client.Send(data, offset, length, SocketFlags.None);
                return true;
            } catch (Exception e) { Console.WriteLine(e.Message); return false; }
        }

        public int Recibir(byte[] buffer) {
            try {
                return this.client.Receive(buffer, 0, buffer.Length, SocketFlags.None);
            } catch (Exception e) { Console.WriteLine(e.Message); return -1; }
        }
    }
}
