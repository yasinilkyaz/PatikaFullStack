package com.patikadev.View;

import com.patikadev.Helper.*;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Patika;
import com.patikadev.Model.User;


import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class OperatorGUI extends JFrame {

    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scrl_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JTextField fld_user_name;
    private JTextField fld_user_uname;
    private JTextField fld_user_pass;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_sh_user_name;
    private JTextField fld_sh_user_uname;
    private JComboBox cmb_sh_user_type;
    private JButton btn_user_sh;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private final Operator operator;
    private  JPopupMenu patikaMenu;
    public OperatorGUI(Operator operator) {
        try {
            Helper.setLayout();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        this.operator = operator;
        add(wrapper);
        setSize(1000, 500);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        lbl_welcome.setText("Hoşgeldin : "+operator.getName());

        // model user list
        mdl_user_list=new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column==0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_user_list={"ID","Ad Soyad","Kullanıcı Adı","Şifre","Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        row_user_list=new Object[col_user_list.length];


        loadUserModel();

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);
        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_user_id=tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString();
                fld_user_id.setText(select_user_id);
            }catch (Exception exp){}
        });
        tbl_user_list.getModel().addTableModelListener(e -> {
       if (e.getType()==TableModelEvent.UPDATE){
           int user_id= Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString());
           String user_name=tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),1).toString();
           String user_uname=tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),2).toString();
           String user_pass=tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),3).toString();
           String user_type=tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),4).toString();

           if(User.update(user_id,user_name,user_uname,user_pass,user_type)){
               Helper.showMsg("done");
           }
           loadUserModel();
       }
        });

        btn_user_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_name)||Helper.isFieldEmpty(fld_user_uname)||Helper.isFieldEmpty(fld_user_pass)){
                Helper.showMsg("fill");
            }else {
                String name=fld_user_name.getText();
                String uname=fld_user_uname.getText();
                String pass=fld_user_pass.getText();
                String type=cmb_user_type.getSelectedItem().toString();
                if (User.add(name,uname,pass,type)){
                    Helper.showMsg("done");
                    loadUserModel();
                    fld_user_name.setText(null);
                    fld_user_uname.setText(null);
                    fld_user_pass.setText(null);
                }
            }
        });

        // Patika list

        patikaMenu =new JPopupMenu();// popup menü oluşturma
        JMenuItem updateMenu=new JMenuItem("Güncelle");
        JMenuItem deleteMenu=new JMenuItem("Sil");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(),0).toString());
            UpdatePatikaGUI updateGUI=new UpdatePatikaGUI(Patika.getFetch(select_id));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                }
            });
        });

        deleteMenu.addActionListener(e -> {
            if(Helper.confirm("sure")){
                int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(),0).toString());
if (Patika.delete(select_id)){
    Helper.showMsg("done");
    loadPatikaModel();
}else {
    Helper.showMsg("error");
    loadPatikaModel();
}

            }

        });


        mdl_patika_list=new DefaultTableModel();
            Object[] col_patika_list={"ID","Patika Adı"};
            mdl_patika_list.setColumnIdentifiers(col_patika_list);
            row_patika_list=new Object[col_patika_list.length];
            loadPatikaModel();

            tbl_patika_list.setModel(mdl_patika_list);
            tbl_patika_list.setComponentPopupMenu(patikaMenu);// gelen veri üzerine sağ click yapıldığında popup menü açılıyor
            tbl_patika_list.getTableHeader().setReorderingAllowed(false);
            tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(50);
            tbl_patika_list.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    Point point=e.getPoint();
                    int selected_row=tbl_patika_list.rowAtPoint(point);
                    tbl_patika_list.setRowSelectionInterval(selected_row,selected_row);
                }
            });

        btn_user_delete.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_user_id)){
                Helper.showMsg("fill");
            }else {
               if (Helper.confirm("sure")){
                    int user_id=Integer.parseInt(fld_user_id.getText());
                    if (User.delete(user_id)){
                        Helper.showMsg("done");
                        loadUserModel();
                    }else {
                        Helper.showMsg("error");
                    }
               }
            }
        });
        btn_user_sh.addActionListener(e -> {
            String name=fld_sh_user_name.getText();
            String uname=fld_sh_user_uname.getText();
            String type=cmb_sh_user_type.getSelectedItem().toString();

            String query=User.searchQuery(name,uname,type);
            loadUserModel(User.searchUserList(query));
        });
        btn_logout.addActionListener(e -> {
        dispose();
        });
        btn_patika_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_patika_name)){
                Helper.showMsg("fill");
            }else {
                if (Patika.add(fld_patika_name.getText())){
                    Helper.showMsg("done");
                    loadPatikaModel();
                    fld_patika_name.setText(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });
    }

    private void loadPatikaModel() {
        DefaultTableModel clearModel= (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for(Patika obj:Patika.getList()){
            i=0;
            row_patika_list[i++]=obj.getId();
            row_patika_list[i++]=obj.getName();
            mdl_patika_list.addRow(row_patika_list);
        }
    }

    public void loadUserModel(){

        DefaultTableModel clearModel= (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj : User.getList()){
            int i=0;
            row_user_list[i++]=obj.getId();
            row_user_list[i++]=obj.getName();
            row_user_list[i++]=obj.getUname();
            row_user_list[i++]=obj.getPass();
            row_user_list[i++]=obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }
    public void loadUserModel(ArrayList<User> list){

        DefaultTableModel clearModel= (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj : list){
            int i=0;
            row_user_list[i++]=obj.getId();
            row_user_list[i++]=obj.getName();
            row_user_list[i++]=obj.getUname();
            row_user_list[i++]=obj.getPass();
            row_user_list[i++]=obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }
    public static void main(String[] args) {
        Helper.setLayout();
        Operator op=new Operator();
        op.setId(1);
        op.setName("Yasin İlkyaz");
        op.setPass("1234");
        op.setType("operator");
        op.setUname("yasin");

        OperatorGUI gui=new OperatorGUI(op);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
