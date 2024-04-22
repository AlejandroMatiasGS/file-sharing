using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace desktop_app.Vista {
    public partial class SelectionControl : UserControl {
        public event EventHandler ControlClick;

        public SelectionControl() {
            InitializeComponent();
        }

        internal void SetName(string name) {
            this.lblName.Text = name;
        }

        internal void SetImage(Image image) {
            this.lblImagen.Image = image;
        }

        private void SelectionControl_MouseEnter(object sender, EventArgs e) {
            this.BackColor = Color.LightGray;
        }

        private void SelectionControl_MouseLeave(object sender, EventArgs e) {
            this.BackColor = SystemColors.Control;
        }

        private void Controls_MouseEnter(object sender, EventArgs e) {
            this.BackColor = Color.LightGray;
        }

        private void Controls_MouseLeave(object sender, EventArgs e) {
            this.BackColor = SystemColors.Control;
        }

        private void Control_Click(object sender, EventArgs e) {
            this.ControlClick.Invoke(this, EventArgs.Empty);
        }
    }
}
