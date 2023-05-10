import javax.sql.rowset.serial.SerialStruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Registration_Form extends JDialog  {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField tfPassword;
    private JPasswordField tfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;
    public Registration_Form(JFrame parent){
        super(parent);
        setTitle("Create a new Account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String name=tfName.getText();
        String email=tfEmail.getText();
        String phone=tfPhone.getText();
        String address=tfAddress.getText();
        String password=String.valueOf(tfPassword.getPassword());
        String confirmPassword=String.valueOf(tfConfirmPassword.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || address.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this,
                    "Confirm Password didn't match",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user=addUserToDatabase(name,email,phone,address,password);
        if (user !=null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);

        }
    }

    public User user;
    private User addUserToDatabase(String name, String email, String phone, String address, String password){
        User user=null;
        final String DB_URL="jdbc:mysql://localhost:3306/login";
        final String USERNAME="root@localhost";
        final String PASSWORD= "Sharma@123";

        try{
            Connection conn= DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            //connected to database successfully..
            Statement statement=conn.createStatement();
            String sql="INSERT INTO users(name,email,phone ,address, password)"+"Values(?,?,?,?,?)";
            PreparedStatement preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,phone);
            preparedStatement.setString(4,address);
            preparedStatement.setString(5,password);

            //insert raw into tables
            int addedRows=preparedStatement.executeUpdate();
            if (addedRows>0){
                user=new User();
                user.name=name;
                user.email=email;
                user.phone=phone;
                user.address=address;
                user.password=password;
            }
            statement.close();
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }


    public static void main(String[] args) {
        Registration_Form myForm=new Registration_Form(null);
        User user= myForm.user;
        if (user!=null){
            System.out.println("Successful registration of: "+user.name);
        }
        else {
            System.out.println("Registration cancelled ");
        }
    }
}
