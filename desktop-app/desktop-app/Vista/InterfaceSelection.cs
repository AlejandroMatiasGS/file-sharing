using desktop_app.Properties;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net.NetworkInformation;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace desktop_app.Vista {
    public partial class InterfaceSelection : Form {
        private int contSelXFila;

        public InterfaceSelection() {
            InitializeComponent();
            contSelXFila = 0;
        }

        public void AddSelectionControl(SelectionControl sc) {
            this.layoutInterfaces.Controls.Add(sc);
            contSelXFila++;

            if (contSelXFila > 1) {
                this.layoutInterfaces.RowCount++;
                this.layoutInterfaces.RowStyles.Add(new RowStyle(SizeType.AutoSize));
                contSelXFila = 0;
            }
        }
    }
}
