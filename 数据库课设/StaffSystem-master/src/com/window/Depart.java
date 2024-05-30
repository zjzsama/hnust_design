package com.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Depart {

    static Vector titleVector=new Vector();
    static Vector departVector=new Vector();
    static JTable departTable;
    static JScrollPane departJScrollPane;
    public static void to()
    {
        JFrame start=new JFrame("部门信息表");
        start.setBounds(600, 200, 350, 400);
        start.setLocationRelativeTo(start.getOwner());
        start.setVisible(true);
        start.setLayout(null);
        start.setResizable(false);
        JPanel pan=new JPanel();
        pan.setBounds(15,15,300,350);
        pan.setLayout(new FlowLayout(FlowLayout.CENTER,0,10));
        titleVector.clear();
        departVector.clear();

        titleVector.add("部门编号");
        titleVector.add("部门名称");
        titleVector.add("部门经理");
        titleVector.add("备注");

        departTable=new JTable(departVector,titleVector);
        departTable.setPreferredScrollableViewportSize(new Dimension(280,300));
        departJScrollPane = new JScrollPane(departTable);
        //分别设置水平和垂直滚动条自动出现
        departJScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        departJScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //监听器
        departTable.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); // 获得行位置
                System.out.println("mouseClicked(). row = " + row);
            }
        });
        QueryAllprocess();
        pan.add(departJScrollPane);
        pan.setOpaque(false);
        start.add(pan);
    }
    private static void QueryAllprocess() {
        try{
            // 建立查询条件
            String sql = "select * from depart ";
            sql+=" order by id asc;";
            System.out.println("queryAllProcess(). sql = " + sql);

            DbProcess.connect();
            ResultSet rs = DbProcess.executeQuery(sql);

            // 将查询获得的记录数据，转换成适合生成JTable的数据形式
            departVector.clear();
            while(rs.next()){
                Vector v = new Vector();
                v.add(Integer.valueOf(rs.getInt("id")));
                v.add(rs.getString("name"));
                v.add(rs.getString("manager"));
                v.add(rs.getString("intro"));
                departVector.add(v);
            }
            departTable.updateUI();
            DbProcess.disconnect();
        }catch(SQLException sqle){
            System.out.println("sqle = " + sqle);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误","错误",JOptionPane.ERROR_MESSAGE);
        }
    }
}
