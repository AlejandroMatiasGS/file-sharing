using Avalonia;
using Avalonia.Controls;
using Avalonia.Media.Imaging;
using System.IO;

namespace desktop_app.Vista;

public partial class Home : Window {
    private Grid grid;
    private int x_;
    private int y_;

    public Home() {
        InitializeComponent();
        InitComponents();

        this.Loaded += Home_Loaded;
    }

    private void InitComponents() {
        x_ = 0;
        y_ = 0;
        grid = new Grid();
        grid.ColumnDefinitions.Add(new ColumnDefinition(GridLength.Auto));
        grid.ColumnDefinitions.Add(new ColumnDefinition(GridLength.Auto));
        grid.RowDefinitions.Add(new RowDefinition(GridLength.Auto));
    }

    private void Home_Loaded(object? sender, Avalonia.Interactivity.RoutedEventArgs e) {
        for (int i = 1; i<=4; i++) {
            SelectionControl sc = new SelectionControl();
            Bitmap image;
            using(var ms = new MemoryStream(desktop_app.Resources.adapter)) { image = new Bitmap(ms); }
            sc.SetImagen(image);
            sc.SetName("HOLLAAAA");
            sc.Margin = new Thickness(7, 7);
            sc.VerticalAlignment = Avalonia.Layout.VerticalAlignment.Top;
            sc.HorizontalAlignment = Avalonia.Layout.HorizontalAlignment.Left;

            if (i % 2 == 0) pnlSel.Children.Add(sc);
            else pnlSel2.Children.Add(sc);

            
        }
    }
}