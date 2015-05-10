/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader_gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ISHFAQ HAIDER QURESHI BS(Software Engineering) IIUI
 */
public class Downloader_GUI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Downloader_GUI dgui = new Downloader_GUI();
    }
    DLMain dlm;
    URL current_url;
    ArrayList<Job> Main_Jobs = new ArrayList();
    Job temp_job;
    int Main_Job_Id = -1; // will be start from 1
    int Selected_Job_Id = -1; // minimum 1
    int Current_Connection = 8;
    String proxy = new String("");
    String port = new String("");
    ProxyFrame proxfr = new ProxyFrame();
    DecimalFormat formate = new DecimalFormat("##.##");
    DefaultTableModel dtm = new DefaultTableModel(0, 4);
    Thread mainthread;
    static String save_dirictory=new String("");
    Downloader_GUI() {

        // Set Proxy        
        //       System.getProperties().put("proxySet", "true");
        //     System.getProperties().put("proxyHost", "192.168.10.50");
        //   System.getProperties().put("proxyPort", "8080");

        /*
         // Remove Proxy
         System.getProperties().put("proxySet", "false");
         System.getProperties().put("proxyHost", "");
         System.getProperties().put("proxyPort", "");
         */


        dlm = new DLMain();
        dlm.setTitle("Light Downloader");
        dlm.jLabelD.setVisible(false);
        dlm.jLabelFS.setVisible(false);
        dlm.jLabelFSA.setVisible(false);
        dlm.jLabelPT.setVisible(false);
        dlm.jLabelTL.setVisible(false);
        dlm.jLabelTR.setVisible(false);
        dlm.jMenu2.setVisible(false);
        dlm.jMenuBar1.setVisible(false);
        dlm.jMenuCon.setVisible(false);
        dlm.jMenuDir.setVisible(false);
        dlm.jMenuProxy.setVisible(false);
        dlm.jProgressBar.setVisible(false);
        dlm.jbStart.setVisible(false);
        dlm.jlDldFig.setVisible(false);
        dlm.jlDldPercentage.setVisible(false);
        dlm.jlRateFig.setVisible(false);
        dlm.jlRateType.setVisible(false);
        dlm.jlSize.setVisible(false);
        dlm.jlTime.setVisible(false);
        dlm.jtfFileName.setVisible(false);
        dlm.jLabelJoin.setVisible(false);

        proxfr.setVisible(false);

        dlm.jMenu2.setVisible(true);
        dlm.jMenuBar1.setVisible(true);
        dlm.jMenuCon.setVisible(false);
        dlm.jMenuDir.setVisible(true);
        dlm.jMenuProxy.setVisible(true);



        dtm.setColumnIdentifiers(new String[]{"Download ID", "File", "Size", "Complete"});

        dlm.jTable.setSelectionMode(0);
        //dlm.jTable.getse

        //dtm.addRow(new String[]{"DownlD","Fe","Se","Comple"});

        //   dtm.addRow(new String[]{"DownlD","Fe","Se","Comple"});


        dlm.jTable.setModel(dtm);


        //dlm.jScrollPane.setVisible(false); 
        //dlm.jTable.//
        //dlm.jTable.setColumnSelectionAllowed(false);
        //dlm.jTable.setEnabled(false);
        //dlm.jTable.setRowSelectionAllowed(true);
        dlm.jMenuDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                        JFileChooser fc = new JFileChooser();
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        fc.showOpenDialog(dlm);
                        try{
                        save_dirictory=fc.getSelectedFile().getAbsolutePath() + "\\" ;}
                        catch (Exception ex){
                        save_dirictory="";
                        }
            }
        });
        dlm.jbGetInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //temp_job.SetJob((++Main_Job_Id), 8, dlm.jtfUrl.getText());
                temp_job = new Job();
                temp_job.SetJob((Main_Job_Id + 1), Current_Connection, dlm.jtfUrl.getText());
//        Main_Jobs.add(new Job((++Main_Job_Id), Current_Connection, dlm.jtfUrl.getText()));
                //System.out.println("Job added");//--
                Main_Jobs.add(temp_job);
                Main_Job_Id++;
                //Selected_Job_Id=Main_Job_Id;
                dlm.jlSize.setText(Main_Jobs.get(Main_Job_Id).Size(Main_Jobs.get(Main_Job_Id).size));
                dlm.jtfFileName.setText(Main_Jobs.get(Main_Job_Id).FileOnly() + "." + Main_Jobs.get(Main_Job_Id).FileExt());
                dlm.jLabelFS.setVisible(true);
                dlm.jlSize.setVisible(true);
                dlm.jLabelFSA.setVisible(true);
                dlm.jtfFileName.setVisible(true);
                dlm.jbStart.setVisible(true);
            }
        });
        dlm.jbStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main_Jobs.get(Main_Job_Id).name = dlm.jtfFileName.getText();
                dlm.jbStart.setVisible(false);
                Main_Jobs.get(Main_Job_Id).StartJob();
                dtm.addRow(new String[]{"" + (Main_Job_Id + 1), "" + Main_Jobs.get(Main_Job_Id).name, ""
                    + Job.Size(Main_Jobs.get(Main_Job_Id).size), "" + Main_Jobs.get(Main_Job_Id).GetState()});
                Selected_Job_Id = Main_Job_Id;
            }
        });
        dlm.jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                Selected_Job_Id = dlm.jTable.getSelectedRow();
                //System.out.println("Selected_Job_Id=" + Selected_Job_Id);//--
            }
        });
        dlm.jMenuProxy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proxfr.jtfAddress.setText(proxy);
                proxfr.jtfPort.setText(port);
                proxfr.jtfCon.setText(""+Current_Connection);
                proxfr.setVisible(true);
            }
        });
        proxfr.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        proxfr.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                proxfr.jtfAddress.setText(proxy);
                proxfr.jtfPort.setText(port);
                proxfr.jtfCon.setText(""+Current_Connection);
                proxfr.setVisible(false);
            }
        });
        proxfr.JbOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (proxfr.jtfAddress.getText() == "" && proxfr.jtfPort.getText() == "") {
                    System.getProperties().put("proxySet", "false");
                    System.getProperties().put("proxyHost", "");
                    System.getProperties().put("proxyPort", "");
                    proxy = port = "";
                } else {
                    proxy = proxfr.jtfAddress.getText();
                    port = proxfr.jtfPort.getText();
                    System.getProperties().put("proxySet", "true");
                    System.getProperties().put("proxyHost", proxy);
                    System.getProperties().put("proxyPort", port);
                }
                try{Current_Connection=Integer.parseInt(proxfr.jtfCon.getText());}
                catch(Exception ex)
                {
                Current_Connection=8;
                }
                proxfr.setVisible(false);
            }
        });
        proxfr.jbCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proxfr.jtfAddress.setText(proxy);
                proxfr.jtfPort.setText(port);
                proxfr.jtfCon.setText(""+Current_Connection);
                proxfr.setVisible(false);
            }
        });
        mainthread = new Thread() {
            public void run() {
                while (true) {
                    UpdateGUI();
                    try {
                        //Thread.currentThread().setPriority(1);
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Downloader_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } // eof while
            }
        };
        mainthread.start();

        /*            
         Job j=new Job(1, 8,"http://softinpc.com/wp-content/uploads/2013/11/Dell-Optiplex-gx280-windows-7-drivers.jpg");
         // Job j=new Job(1, 8,"http://199.217.118.144/Getintopc.com/Adobe_After_Effects_CS3_Setup.zip");
    
         j.StartJob();
         */
        dlm.setVisible(true);
    }

    void UpdateGUI() {
        if (Selected_Job_Id >= 0 && Main_Jobs.get(Selected_Job_Id).check_my_status == true) {
            //System.out.println("UpdateGUI: Main");//--
            dtm.setValueAt("" + Main_Jobs.get(Selected_Job_Id).GetState(), Selected_Job_Id, 3);
            if (Selected_Job_Id >= 0 && Main_Jobs.get(Selected_Job_Id).state == 1) // mean there is any job which is running
            {   // Main_Jobs.get(Selected_Job_Id).Update_State();
                int pb = (int) ((dlm.jProgressBar.getMaximum() / 100) * Main_Jobs.get(Selected_Job_Id).Compelted_Percentage());
                String dld = Job.Size(Main_Jobs.get(Selected_Job_Id).downloaded);
                String dld_prcn = formate.format(Main_Jobs.get(Selected_Job_Id).Compelted_Percentage());
                String tr = Job.Size((long) (Main_Jobs.get(Selected_Job_Id).transfer_rate * 1000));
                String rtim = "" + Job.Time(Main_Jobs.get(Selected_Job_Id).remaining_time_ms);
                dlm.jProgressBar.setValue(pb);
                dlm.jlDldFig.setText(dld);
                dlm.jlDldPercentage.setText(dld_prcn);
                dlm.jlRateFig.setText(tr);
                dlm.jlTime.setText(rtim);

                //System.out.println("PB: "+pb+", DLD: "+dld+", %: "+dld_prcn+", speed: "+tr+", rem: "+rtim);//--

                dlm.jProgressBar.setVisible(true);
                dlm.jLabelD.setVisible(true);
                dlm.jlDldFig.setVisible(true);
                dlm.jlDldPercentage.setVisible(true);
                dlm.jLabelPT.setVisible(true);
                dlm.jLabelTR.setVisible(true);
                dlm.jlRateFig.setVisible(true);
                dlm.jlRateType.setVisible(true);
                dlm.jLabelTL.setVisible(true);
                dlm.jlTime.setVisible(true);

                dlm.jLabelJoin.setVisible(false);
            } else if (Selected_Job_Id >= 0 && Main_Jobs.get(Selected_Job_Id).state == 2) // mean the selected file is in joining process
            {
                dlm.jProgressBar.setVisible(true);
                dlm.jLabelD.setVisible(false);
                dlm.jlDldFig.setVisible(false);
                dlm.jlDldPercentage.setVisible(false);
                dlm.jLabelPT.setVisible(false);
                dlm.jLabelTR.setVisible(false);
                dlm.jlRateFig.setVisible(false);
                dlm.jlRateType.setVisible(false);
                dlm.jLabelTL.setVisible(false);
                dlm.jlTime.setVisible(false);
                //dlm.jLabelTR
                dlm.jProgressBar.setValue((int) ((dlm.jProgressBar.getMaximum() / 100) * Main_Jobs.get(Selected_Job_Id).Joining_Percentage()));
                dlm.jLabelJoin.setVisible(true);
            } else if (Selected_Job_Id >= 0 && Main_Jobs.get(Selected_Job_Id).state == 3) {
                dlm.jProgressBar.setVisible(false);
                dlm.jLabelD.setVisible(false);
                dlm.jlDldFig.setVisible(false);
                dlm.jlDldPercentage.setVisible(false);
                dlm.jLabelPT.setVisible(false);
                dlm.jLabelTR.setVisible(false);
                dlm.jlRateFig.setVisible(false);
                dlm.jlRateType.setVisible(false);
                dlm.jLabelTL.setVisible(false);
                dlm.jlTime.setVisible(false);
                dlm.jLabelJoin.setVisible(false); // Joining Label

                /*dlm.jLabelFS.setVisible(false);
                 dlm.jLabelFSA.setVisible(false);
                 dlm.jbStart.setVisible(false);
                 dlm.jlSize.setVisible(false);
                 dlm.jtfFileName.setVisible(false);*/
            }
            Main_Jobs.get(Selected_Job_Id).check_my_status = false;
        }
    }
}

class Job {
//final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f','`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r' , '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' }; 
//final String [] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f','`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
    int NumOfCon = 8; // by default num of connection
    URL url;
    File file;
    Long size = new Long(0); // total size in bytes
    Long downloaded = new Long(0); // downloaded bytes
    Long joining = new Long(0); // joining bytes    
    Float transfer_rate = new Float(0); // bytes per mili second
    Long resume_time = Long.MAX_VALUE;
    Long last_update_time = Long.MAX_VALUE;
    Long remaining_time_ms = Long.MAX_VALUE;
    int state = 0;            // 0:not start downloading, 
    // 1: downloading is in progress
    // 2: downloading complete, joining is in progress
    // 3: downloading and joining complete, finish

    String GetState() {
        String ret = new String("");
        switch (state) {
            case 0:
                ret = "Not Start";
                break;
            case 1:
                ret = "Downloading";
                break;
            case 2:
                ret = "Joining";
                break;
            case 3:
                ret = "Finish";
                break;
        }
        return ret;
    }
    ArrayList<Conn> jobList = new ArrayList();
    Boolean check_my_status = new Boolean(false); // set this flag to inform main program that 
    //check my latest stats. i.e transfer rate, remaining time
    int JobIndex = 0;
    String name = new String("");
File opt;
    Job() {
    }

    Job(int ji, int cn, String ur) {
        JobIndex = ji;
        NumOfCon = cn;
        HttpURLConnection uc;
        try {
            System.out.println("Construct: 1");
            url = new URL(ur);
            uc = (HttpURLConnection) url.openConnection();
            size = Long.parseLong(String.valueOf(uc.getContentLength()));
            name=uc.getHeaderField("Content-Disposition");
            if(name==null)
            name = "" + url.toString().substring(url.toString().lastIndexOf('/') + 1, url.toString().length());
            MakeCorrectName();
            uc.disconnect();
            //System.out.println(FileOnly()+"."+FileExt());//--

        } catch (MalformedURLException ex) {
            Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void SetJob(int ji, int cn, String ur) {
        JobIndex = ji;
        NumOfCon = cn;
        HttpURLConnection uc;
        try {
            url = new URL(ur);
            uc = (HttpURLConnection) url.openConnection();
            size = Long.parseLong(String.valueOf(uc.getContentLength()));
            name=uc.getHeaderField("Content-Disposition");
            if(name==null)
            name = "" + url.toString().substring(url.toString().lastIndexOf('/') + 1, url.toString().length());
            MakeCorrectName();
            uc.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    int StartJob_int1;
    void StartJob() {
        MakeCorrectName();
        Conn temp;
        Long st = new Long(-1);
        Long en = new Long(-1);
        state = 1;
        resume_time = System.currentTimeMillis();
        last_update_time = System.currentTimeMillis();
        //opt=new File(""+FileOnly()+"."+FileExt());
        for (int i = 1; i <= NumOfCon; i++) {
            st = (i - 1) * ((size + 1) / NumOfCon);
            en = ((i * ((size + 1) / NumOfCon)) - 1) + ((i / NumOfCon) * (size - ((i * ((size + 1) / NumOfCon)) - 1)));

            //temp = new Conn(i, url.toString(), st, en, (JobIndex + "part" + i));
            temp = new Conn(i, url.toString(), st, en, (FileOnly()+"."+FileExt()));
            
            jobList.add(temp);
            StartJob_int1 = i;
            /*
             new Thread(){
             @Override
             public void run(){
             Thread.currentThread().setPriority(10);
             jobList.get(Con_JL_Index(StartJob_int1)).run();
             //temp.run();
             }
             }.start();
             */
            //StartJob_int1=i;
            //jobList.get(Con_JL_Index(StartJob_int1)).start();
            temp.setPriority(10);
            temp.start();
            //System.out.println("In Job.Start() Loop="+i);//--
        }
        //System.out.println("eof Job.Start()");//--
        new Thread() {
            public void run() {
                while (!All_Finish().equals(true)) {
                    //Update_State();
                    Thread.currentThread().setPriority(1);
                    if (check_my_status == false) {
                        //System.out.println("Job Joining thread");//--
                        Update_State();
                    }

                    try {
                        Thread.sleep(1000);
                        //Thread.yield();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                Thread.currentThread().setPriority(5);
                state = 2;
                //Join();
                
                state = 3;
                check_my_status = true;                
            }
        }.start();
    }

    Boolean All_Finish() {
        for (int i = 0; i < NumOfCon; i++) {
            if (jobList.get(i).finish == false) {
                return false;
            }
        }
        return true;
    }

    void Join() {
        FileInputStream fio;
        File ip;
        File op = new File(Downloader_GUI.save_dirictory+FileOnly() + "." + FileExt());
        int t_dat;
        try {
            FileOutputStream fos = new FileOutputStream(op);
            for (int i = 1; i <= NumOfCon; i++) {
                ip = new File(JobIndex + "part" + i);
                fio = new FileInputStream(ip);
                float pre = 0;
                while ((t_dat = fio.read()) != -1) {
                    fos.write(t_dat);
                    joining++;
                    if (Joining_Percentage() >= (pre + 1)) {
                        pre++;
                        check_my_status = true;
                    }
                }
                fio.close();
                fio = null;
            }
            fos.close();
            fos = null;
            op = null;
            for (int i = 1; i <= NumOfCon; i++) {
                ip = new File(JobIndex + "part" + i);
                while (!ip.delete()) {
                }
                //System.out.println(JobIndex+"part"+i+" Deleted***");//--
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    void Update_State() {
        /*    Float temp_t=new Float(0);
         Float temp_b=new Float(0);    
         temp_t=Float.parseFloat(Long.toString(System.currentTimeMillis()-last_update_time));
         last_update_time+=Long.parseLong(Float.toString(temp_t));
         temp_b=Float.parseFloat(Long.toString(CalculateDownloaded()-downloaded)); // difference
         downloaded+=Long.parseLong(Float.toString(temp_b)); // add difference to total downloaded bytes
         transfer_rate=temp_b/temp_t;

         */
        Long temp_t = new Long(0);
        Long temp_b = new Long(0);
        temp_t = System.currentTimeMillis() - last_update_time;
        last_update_time += temp_t;
        temp_b = CalculateDownloaded() - downloaded; // difference
        downloaded += temp_b; // add difference to total downloaded bytes

        transfer_rate = (float) temp_b / temp_t;
        remaining_time_ms = (long) ((size - downloaded) / transfer_rate);
        check_my_status = true;
    }

    int Con_JL_Index(int f) {
        for (int i = 0; i < jobList.size(); i++) {
            if (jobList.get(i).indx == f) {
                return i;
            }
        }
        return -1;
    }
    void MakeCorrectName(){
        int k=-1;
        for(int i=0;i<ILLEGAL_CHARACTERS.length;i++)
        while((k=name.lastIndexOf(Character.toString(ILLEGAL_CHARACTERS[i])))!=-1)    
        name=name.substring(0,k)+name.substring(k+1,name.length());
    }
    String FileOnly() {
        if (name.lastIndexOf('.') != -1) {
            return name.substring(0, (name.lastIndexOf('.')));
        } else {
            return name;
        }
    }

    String FileExt() {
        if (name.lastIndexOf('.') != -1) {
            return name.substring((name.lastIndexOf('.') + 1), name.length());
        } else {
            return "";
        }
    }

    static String Size(Long sme) {
        String siz = new String("");
        Float temp = new Float(sme);
        DecimalFormat f = new DecimalFormat("###.##");
        if (sme >= (1024 * 1024 * 1024)) {
            temp = temp / (1024 * 1024 * 1024);
            siz = f.format(temp) + " GB";
        } else if (sme >= (1024 * 1024)) {
            temp = temp / (1024 * 1024);
            siz = f.format(temp) + " MB";
        } else if (sme >= (1024)) {
            temp = temp / (1024);
            siz = f.format(temp) + " KB";
        } else if (sme >= 0 && sme < 1024) {
            siz = f.format(temp) + " Bytes";
        }
        return siz;
    }

    Float Compelted_Percentage() {
        return new Float(((new Double(downloaded)) / (new Double(size))) * 100);
    }

    Float Joining_Percentage() {
        return new Float(((new Double(joining)) / (new Double(size))) * 100);
    }

    Long CalculateDownloaded() {
        Long temp = new Long(0);
        for (int i = 0; i < jobList.size(); i++) {
            temp += jobList.get(i).count;
        }
        return temp;
    }

    static String Time(Long mses) {
        String temp = new String("");
        if (mses > (24 * 60 * 60 * 1000)) {
            return "Undefined";
        }
        if (mses >= (60 * 60 * 1000)) {
            temp += (mses / (60 * 60 * 1000)) + "H";
            mses %= (60 * 60 * 1000);
        }
        if (mses >= (60 * 1000)) {
            temp += " " + (mses / (60 * 1000)) + "M";
            mses %= (60 * 1000);
        }
        if (mses >= (1000)) {
            temp += " " + (mses / (1000)) + "S";
        }
        return temp;
    }
}

class Conn extends Thread {

    int indx = -1; // which part is this of main file
    URL url;
    Long start; // starting byte
    Long end; // ending byte
    Long count = new Long(0);
    int data = -1; // curent byte(data on byte), not sequence num of byte
    HttpURLConnection uc;
    RandomAccessFile file;

    Thread thread;
    InputStream ips;
    FileOutputStream fos;
    Boolean finish = new Boolean(false);

    Long Rem_Bytes() {
        return (end - count);
    }

    Conn(int i, String path, Long st, Long en, String fl) {
        try {
            indx = i;
            start = new Long(st);
            end = new Long(en);
            url = new URL("" + path);
            file = new RandomAccessFile(fl,"rw");
            file.seek(start);
            //System.out.println("eof Conn(constructor)");//--

        } catch (MalformedURLException ex) {
            Logger.getLogger(Conn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Conn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Conn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void OpenConnection() {
        try {
            start = start + count;
            uc = (HttpURLConnection) url.openConnection();
            uc.setRequestProperty("Range", "bytes=" + start + "-" + end);
            uc.connect();

        } catch (IOException ex) {
            Logger.getLogger(Conn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void CloseConnection() {
        uc.disconnect();
    }

    public void run() {
        OpenConnection();
        try {
            ips = uc.getInputStream();
                //RandomAccessFile file2=new RandomAccessFile(file,"rw");
            //fos = new FileOutputStream(file2);
              //file2.seek(start);
            while ((data = ips.read()) != -1) {   //System.out.println("Connection:"+indx);//--
                //fos.write(data);
                file.write(data);
                count++;
            }
            //fos.close();
            file.close();
            finish = true;
            //System.out.println("eof Conn.Start()");//--
        } catch (IOException ex) {
            Logger.getLogger(Conn.class.getName()).log(Level.SEVERE, null, ex);
        }
        CloseConnection();
    }
}