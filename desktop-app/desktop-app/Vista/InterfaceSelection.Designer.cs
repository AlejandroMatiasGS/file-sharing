namespace desktop_app.Vista {
    partial class InterfaceSelection {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent() {
            this.layoutInterfaces = new System.Windows.Forms.TableLayoutPanel();
            this.SuspendLayout();
            // 
            // layoutInterfaces
            // 
            this.layoutInterfaces.AutoScroll = true;
            this.layoutInterfaces.ColumnCount = 2;
            this.layoutInterfaces.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.layoutInterfaces.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.layoutInterfaces.Location = new System.Drawing.Point(2, 2);
            this.layoutInterfaces.Name = "layoutInterfaces";
            this.layoutInterfaces.RowCount = 1;
            this.layoutInterfaces.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.layoutInterfaces.Size = new System.Drawing.Size(748, 257);
            this.layoutInterfaces.TabIndex = 0;
            // 
            // InterfaceSelection
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.ClientSize = new System.Drawing.Size(752, 261);
            this.Controls.Add(this.layoutInterfaces);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "InterfaceSelection";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Seleccione una interfaz";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TableLayoutPanel layoutInterfaces;
    }
}