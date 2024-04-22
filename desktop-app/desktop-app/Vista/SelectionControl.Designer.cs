namespace desktop_app.Vista {
    partial class SelectionControl {
        /// <summary> 
        /// Variable del diseñador necesaria.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Limpiar los recursos que se estén usando.
        /// </summary>
        /// <param name="disposing">true si los recursos administrados se deben desechar; false en caso contrario.</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Código generado por el Diseñador de componentes

        /// <summary> 
        /// Método necesario para admitir el Diseñador. No se puede modificar
        /// el contenido de este método con el editor de código.
        /// </summary>
        private void InitializeComponent() {
            this.lblImagen = new System.Windows.Forms.Label();
            this.lblName = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // lblImagen
            // 
            this.lblImagen.Location = new System.Drawing.Point(7, 7);
            this.lblImagen.Name = "lblImagen";
            this.lblImagen.Size = new System.Drawing.Size(96, 96);
            this.lblImagen.TabIndex = 0;
            this.lblImagen.Click += new System.EventHandler(this.Control_Click);
            this.lblImagen.MouseEnter += new System.EventHandler(this.Controls_MouseEnter);
            this.lblImagen.MouseLeave += new System.EventHandler(this.Controls_MouseLeave);
            // 
            // lblName
            // 
            this.lblName.Font = new System.Drawing.Font("Comic Sans MS", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblName.Location = new System.Drawing.Point(109, 7);
            this.lblName.Name = "lblName";
            this.lblName.Size = new System.Drawing.Size(243, 96);
            this.lblName.TabIndex = 1;
            this.lblName.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.lblName.Click += new System.EventHandler(this.Control_Click);
            this.lblName.MouseEnter += new System.EventHandler(this.Controls_MouseEnter);
            this.lblName.MouseLeave += new System.EventHandler(this.Controls_MouseLeave);
            // 
            // SelectionControl
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.Controls.Add(this.lblName);
            this.Controls.Add(this.lblImagen);
            this.Name = "SelectionControl";
            this.Size = new System.Drawing.Size(358, 108);
            this.Click += new System.EventHandler(this.Control_Click);
            this.MouseEnter += new System.EventHandler(this.SelectionControl_MouseEnter);
            this.MouseLeave += new System.EventHandler(this.SelectionControl_MouseLeave);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Label lblImagen;
        private System.Windows.Forms.Label lblName;
    }
}
