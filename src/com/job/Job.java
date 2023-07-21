package com.job;

import com.link.Link;
import java.lang.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.resource.*; // cci.jar

/* Import VSE Connector classes */
import com.ibm.vse.connector.*;
/* Included in classpath. Uncomment for use */
// import com.ibm.vse.connector.lowlevel.*;
// import com.ibm.vse.connector.lowlevel.data.*;
// import com.ibm.vse.connector.lowlevel.data.power.*;

/**
 * Class Job
 * @version 1.0
 * Submits jobs to VSE System
 */
public class Job
{
   protected static VSEConnectionSpec spec;
   protected static VSESystem system;

   /**
    */
   public static void main(String argv[]) throws IOException, ResourceException, CreateFailedException
   {   
      /* USED FOR CREATING A JOB IN MEMORY */
      //JobInputStream job;
      //JobOutputStream dest;
      //Vector vLines;
      String sys, ipAddr, userID, password;
      VSEPower power;
      File jobFileOut, outFile;
      Link linker;
      System.out.println("Creating file");
      Writer fileWriter = new FileWriter("../../../out.txt", false);
      fileWriter.close();
      try {
        BufferedWriter jobFile = new BufferedWriter(new FileWriter("../../../out.job", false));
        BufferedReader jcl1 = new BufferedReader(new FileReader("../../../jcl.txt"));
        BufferedReader cob = new BufferedReader(new FileReader("../../../cob.txt"));
        BufferedReader jcl2 = new BufferedReader(new FileReader("../../../jcl2.txt"));

        String str;
        while ((str = jcl1.readLine()) != null) {
            jobFile.write(str+"\n");
            }
        jcl1.close();
        while ((str = cob.readLine()) != null) {
            jobFile.write(str+"\n");
            }
        cob.close();
        while ((str = jcl2.readLine()) != null) {
            jobFile.write(str+"\n");
            }
        jcl2.close();
        jobFile.close();
        } catch (IOException e) {
            System.out.println("Error Linking Code to JCL");
            System.out.println(e);
        }

      /* Prompt for user ID and password. */
      BufferedReader r = new BufferedReader(
                               new InputStreamReader(System.in));
      System.out.println("Select LPAR ('prod' or 'test'):");
      sys = r.readLine();
      if(sys == "prod"){
        ipAddr = "195.0.50.202";
      }else{
        ipAddr = "195.0.50.203";
      }
      System.out.println("Please enter your VSE user ID:");
      userID = r.readLine();
      System.out.println("Please enter password:");
      password = r.readLine();

      /* Create connection specification. */
      try {
         spec = new VSEConnectionSpec(InetAddress.getByName(ipAddr),
                                              2893, userID, password);
      }
      catch (UnknownHostException e)
      {
         System.out.println("Unknown host : " + e);
         return;
      }

      /* Stay logon with this user for lifetime of this connection */
      spec.setLogonMode(true);

      /* Create VSE system instance with this connection */
      system = new VSESystem(spec);

      /* Keep the connection for lifetime of this program */
      system.setConnectionMode(true);      

      /* Get POWER reader queue object from host */
      power = system.getVSEPower();
      VSEConsole console = new VSEConsole(system);
      //console.open();
      /* 1. Submit a job file that is stored on the local disk */
      /* The execute() method returns as soon as the job output has been */
      /* transferred from the POWER list queue to outFile. */
      System.out.println("1. Submitting test.job");
      jobFileOut = new File("../../../test.job");
      outFile = new File("../../../console.txt");
      try{
        power.executeJob(jobFileOut, outFile);
      }catch(com.ibm.vse.connector.CreateFailedException cfe){

      }
      //System.out.println(console.getMessage());
      System.out.println("Output is now in out.txt in current directory.");
      System.out.println(console.getMessage());
      /* 2. Create the job file in memory and send it to the host. */
      /* This has the advantage, that we don't need any access to */
      /* the local file system. */
    //     System.out.println("-------------------------------------");
    //     System.out.println("2. Creating job in memory...");
    //     job = new JobInputStream();
    //     dest = new JobOutputStream();
    //     power.executeJob(job, dest);
    //     vLines = dest.getAllLines();
    //     System.out.println("Number of output lines: " + new Integer(vLines.size()).toString());
    //   for (int i=0;i<vLines.size();i++)
    //   {
    //     System.out.println((String)(vLines.elementAt(i)));
    //   }
   }
}