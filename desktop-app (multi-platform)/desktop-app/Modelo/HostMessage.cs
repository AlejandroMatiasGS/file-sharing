using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace desktop_app.Modelo {
    internal class HostMessage {
        public readonly static byte[] WAIT = new byte[] { 0 };
        public readonly static byte[] PROCEED = new byte[] { 1 };
    }
}
