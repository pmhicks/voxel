import javax.swing.*;

public class Voxel extends JFrame {
    
    
    
    public Voxel() {
        initUI();
    }
    
    private void initUI() {
        
        setTitle("Voxel");
        //setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        VoxelPanel vp = new VoxelPanel();
        add(vp);
        setResizable(false);
        pack();
        addKeyListener(new VoxelKeyListener(vp));
        vp.start();
    }
    
    public static void main(String[] args) {
                
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Voxel v = new Voxel();
                v.setVisible(true);
            }
        });
    }
}
