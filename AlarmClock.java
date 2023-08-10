package org.project.charan;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.Font;
import javax.swing.border.MatteBorder;
import java.awt.Color;
public class AlarmClock extends JFrame implements ActionListener {
    private final JLabel timeLabel;
    private final JTextField hoursTextField;
    private final JTextField minutesTextField;
    private final JButton addButton;
    private final JButton removeButton;
    private final JList<String> alarmList;
    private final DefaultListModel<String> alarmListModel;
    private final Timer timer;
    private final List<Calendar> alarms;
    public AlarmClock() {
        super("Alarm Clock");
        alarms = new ArrayList<>();
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Jua", Font.BOLD, 40));
        JPanel timePanel = new JPanel();
        timePanel.setBorder(new MatteBorder(5, 5, 5, 5, (Color) new Color(0, 0, 0)));
        timePanel.add(timeLabel);
        JLabel hoursLabel = new JLabel("Hours:");
        hoursTextField = new JTextField(2);
        JLabel minutesLabel = new JLabel("Minutes:");
        minutesTextField = new JTextField(2);
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);
        JPanel alarmPanel = new JPanel();
        alarmPanel.setBorder(new MatteBorder(0, 5, 5, 5, (Color) new Color(0, 0, 0)));
        alarmPanel.add(hoursLabel);
        alarmPanel.add(hoursTextField);
        alarmPanel.add(minutesLabel);
        alarmPanel.add(minutesTextField);
        alarmPanel.add(addButton);
        alarmPanel.add(removeButton);
        alarmListModel = new DefaultListModel<>();
        alarmList = new JList<>(alarmListModel);
        alarmList.setBorder(new MatteBorder(0, 5, 0, 5, (Color) new Color(0, 0, 0)));
        JScrollPane scrollPane = new JScrollPane(alarmList);
        getContentPane().add(timePanel, BorderLayout.NORTH);
        getContentPane().add(alarmPanel, BorderLayout.SOUTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        setSize(374, 290);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        updateClock();
        updateAlarmList();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClock();
                checkAlarms();
            }
        });
        timer.start();
    }
    // Implementing the actionslistener method for add and remove buttons
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            int hours = Integer.parseInt(hoursTextField.getText());
            int minutes = Integer.parseInt(minutesTextField.getText());
            Calendar alarm = Calendar.getInstance();
            alarm.set(Calendar.HOUR_OF_DAY, hours);
            alarm.set(Calendar.MINUTE, minutes);
            alarm.set(Calendar.SECOND, 0);
            alarms.add(alarm);
            updateAlarmList();
        } else if (e.getSource() == removeButton) {
            int selectedIndex = alarmList.getSelectedIndex();
            if (selectedIndex != -1) {
                alarms.remove(selectedIndex);
                updateAlarmList();
            }
        }
    }
    // Method to update the time of the clock
    private void updateClock() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = dateFormat.format(new Date());
        timeLabel.setText(time);
    }
    //method to update the alarm list
    private void updateAlarmList() {
        alarmListModel.clear();
        for (Calendar alarm : alarms) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String alarmTime = dateFormat.format(alarm.getTime());
            alarmListModel.addElement(alarmTime);
        }
    }
    // Method to check if the alarm time is elapsed or equal to current time
    private void checkAlarms() {
        try{for (Calendar alarm : alarms) {
            if (alarm.getTimeInMillis() <= System.currentTimeMillis()) {
                ringAlarm(alarm);
                alarms.remove(alarm);
                updateAlarmList();
//   resume the timer
                timer.start();
            }
        }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(addButton, "Alarm removed");
            e.printStackTrace();
        }
    }
    //Method to ring the alarm
    private void ringAlarm(Calendar alarm) {
// stop the timer from updating the clock while the alarm is ringing
        timer.stop();
// Ring the bell
        try {
            for(int i=0;i<10;i++) {
                Toolkit.getDefaultToolkit().beep();
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
// display a message dialog to alert the user
        JOptionPane.showMessageDialog(this, "Time's up!", "Alarm", JOptionPane.INFORMATION_MESSAGE);
    }
    // Main method
    public static void main(String[] args) {
        new AlarmClock();
    }
}