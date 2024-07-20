using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;
using Avalonia.Media.Imaging;

namespace desktop_app.Vista;

public partial class SelectionControl : UserControl {
    public SelectionControl() {
        InitializeComponent();
    }

    public void SetImagen(Bitmap image) {
        lblImagen.Source = image;
    }

    public void SetName(string name) {
        lblName.Text = name;
    }
}