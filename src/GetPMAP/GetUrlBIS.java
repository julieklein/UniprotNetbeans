package GetPMAP;

import java.io.*;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
import java.io.PrintStream;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * @author julieklein
 */
public class GetUrlBIS {
    
    

    public GetUrlBIS() {
        /**
         * @param args the command line arguments
         */
//         
        URL u;
        InputStream is = null;
        DataInputStream dis;
        String s = null;
        StringBuilder sbd = new StringBuilder();

        PrintStream csvWriter = null;
        LinkedList<CleavageSiteDBEntryPMAP> alldata = new LinkedList<CleavageSiteDBEntryPMAP>();

        try {

            u = new URL("file:///Users/julieklein/Desktop/ProteasiX/PMAP/search_result_p11.htm");          
            is = u.openStream();
            dis = new DataInputStream(new BufferedInputStream(is));
            while ((s = dis.readLine()) != null) {
                s = s.replaceAll("\\?", "");               
                sbd.append(s);
            }

        } catch (MalformedURLException mue) {

            System.out.println("Ouch - a MalformedURLException happened.");
            mue.printStackTrace();
            System.exit(1);

        } catch (IOException ioe) {

            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
            System.exit(1);

        } finally {

            try {
                is.close();
            } catch (IOException ioe) {
               
            }

        }



        String string = sbd.toString();

        String expr = "(<input\\s+id=\"ballot.*?>Detail</a></td>)";
        StringBuilder sbd2 = new StringBuilder();
        Pattern p = Pattern.compile(expr,
                Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
        Matcher m = p.matcher(string);
        while (m.find()) {
            String entry = m.group(1);
            entry = entry + "\n" + "******************************" + "\n";
            //System.out.println(entry);
            sbd2.append(entry);

        }



        String string2 = sbd2.toString();


        String expr2 = "<td><a\\s+href=\""
                + "([^\"]+)"
                + "\"[^>]*>";
        Pattern p2 = Pattern.compile(expr2,
                Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
        Matcher m2 = p2.matcher(string2);

        while (m2.find()) {
            String url = m2.group(1);
            CleavageSiteDBEntryPMAP csdb = new CleavageSiteDBEntryPMAP();


            url = "http://cutdb.burnham.org" + url;
            csdb.setCsurl(url);
            System.out.println("\n" + "******************************" + url);

            URL u2;
            InputStream is2 = null;
            DataInputStream dis2;
            String s2 = null;
            StringBuilder sbd3 = new StringBuilder();

            try {

                u2 = new URL(url);


                is2 = u2.openStream();         // throws an IOException


                dis2 = new DataInputStream(new BufferedInputStream(is2));

                while ((s2 = dis2.readLine()) != null) {
                    s2 = s2.replaceAll("\\?", "");
                    //System.out.println(s2);
                    sbd3.append(s2);
                }

            } catch (MalformedURLException mue) {

                System.out.println("Ouch - a MalformedURLException happened.");
                mue.printStackTrace();
                System.exit(1);

            } catch (IOException ioe) {

                System.out.println("Oops- an IOException happened.");
                ioe.printStackTrace();
                System.exit(1);

            } finally {

                try {
                    is2.close();
                } catch (IOException ioe) {
                    // just going to ignore this one
                }

            } // end of 'finally' clause

            String string3 = sbd3.toString();

            String expr3 = "Substrate[^<]+</th>[^<]+<td>[^<]+<table>[^<]+<tr>[^<]+<th\\s+class=\"th3\">Definition:</th>[^<]+<td><b>"
                    + "([^<]+)";



            Pattern p3 = Pattern.compile(expr3,
                    Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
            Matcher m3 = p3.matcher(string3);
            
            SubstrateDBEntryPMAP sdb = new SubstrateDBEntryPMAP();
            csdb.setSubstrate(sdb);

            if (m3.find()) {
                String proteinname = m3.group(1);
                proteinname = proteinname.trim();
                proteinname = proteinname.replaceAll(",","");
                sdb.setSubstrateprotname(proteinname);
                System.out.println(proteinname);
            } else {
                String proteinname = "";
                sdb.setSubstrateprotname(proteinname);
                System.out.println(proteinname);
            }

            String expr31 = "Substrate[^<]+</th>[^<]+<td>[^<]+<table>[^<]+<tr>[^<]+<th\\s+class=\"th3\">Definition:</th>[^<]+<td><b>"
                    + "[^<]+"
                    + "</b></td>[^<]+<tr>[^<]+<th\\s+class=\"th3\">Symbol:</th>"
                    + "([^<]+<td><b>)?([^<]+)";


            Pattern p31 = Pattern.compile(expr31,
                    Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
            Matcher m31 = p31.matcher(string3);
            
            if (m31.find()) {
                String proteinsymbol = m31.group(2);
                sdb.setSubstratename(proteinsymbol);
                System.out.println(proteinsymbol);
            } else {
                String proteinsymbol = "";
                sdb.setSubstratename(proteinsymbol);
                System.out.println(proteinsymbol);
            }



            String expr4 = "<div\\s+id=\"protdata\">[^<]+<table>[^<]+<tr>[^<]+<th\\s+class=\"th3\">Definition:</th>[^<]+<td><b>"
                    + "([^<]+)";



            Pattern p4 = Pattern.compile(expr4,
                    Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
            Matcher m4 = p4.matcher(string3);


            if (m4.find()) {
                String enzymename = m4.group(1);
                ProteaseDBEntryPMAP pdb = new ProteaseDBEntryPMAP();
                csdb.setProtease(pdb);
                String string400 = enzymename.trim();
                string400 = string400.replaceAll(",","");
                pdb.setEnzymename(string400);
                System.out.println(string400);
            } else {
                String string400 = "";
                ProteaseDBEntryPMAP pdb = new ProteaseDBEntryPMAP();
                csdb.setProtease(pdb);
                pdb.setEnzymename(string400);
                System.out.println(string400);
            }


            String expr8 = "UniProt\\s+Accession:</th>[^<]+<td><a\\s+href\\s+=\\s+\"[^\"]+\"\\s+target=\"[^\"]+\">"
                    + "([^<]+)";

            Pattern p8 = Pattern.compile(expr8,
                    Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
            Matcher m8 = p8.matcher(string3);
            
            if (m8.find()) {
                String accession = m8.group(1);
                accession = accession.trim();
                sdb.setSubstrateacessionlist(accession);
                System.out.println(accession);
            } else {
                String accession = "";
                sdb.setSubstrateacessionlist(accession);
                System.out.println(accession);
            }


            String expr5 = "<div\\s+id=\"cleav2\">[^<]+<table>[^<]+<th\\s+class=\"th3\">Position:</th>[^<]+<td>"
                    + "([^<]+)?";


            Pattern p5 = Pattern.compile(expr5,
                    Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
            Matcher m5 = p5.matcher(string3);
            while (m5.find()) {
                String position = m5.group(1);
                position = position.trim();
                if (position.equalsIgnoreCase("No_information")) {
                    int intP1 = 0;
                    csdb.setP1(intP1);
                    int intP1prime = 0;
                    csdb.setP1prime(intP1prime);
                    System.out.println(intP1);
                    System.out.println(intP1prime);
                } else {

                    //System.out.println(position);
                    String positionSplit[] = position.split("-");
                    String P1 = positionSplit[0];
                    String P1prime = positionSplit[1];
                    int intP1 = Integer.parseInt(P1);
                    csdb.setP1(intP1);
                    int intP1prime = Integer.parseInt(P1prime);
                    csdb.setP1prime(intP1prime);
                    System.out.println(intP1);
                    System.out.println(intP1prime);
                }

            }

            String expr6 = "<div\\s+id=\"cleav\">[^<]+<table>[^<]+<th\\s+class=\"th3\">Sequence:</td>[^<]+<td>"
                    + "([^<]+)?";


            Pattern p6 = Pattern.compile(expr6,
                    Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
            Matcher m6 = p6.matcher(string3);
            while (m6.find()) {
                String sequence = m6.group(1);
                sequence = sequence.trim();

                if (sequence.equalsIgnoreCase("No_information")) {
                    String aa1 = "?";
                    String aa1prime = "?";
                    char aaP1 = aa1.charAt(0);
                    char aaP1prime = aa1prime.charAt(0);
                    csdb.setAaP1(aaP1);
                    csdb.setAaP1prime(aaP1prime);
                    System.out.println(aaP1);
                    System.out.println(aaP1prime);
                } else {
                    //System.out.println(position);
                    String positionSplit[] = sequence.split("");
                    String aa1 = positionSplit[4];
                    char aaP1 = aa1.charAt(0);
                    String aa1prime = positionSplit[6];
                    char aaP1prime = aa1prime.charAt(0);
                    csdb.setAaP1(aaP1);
                    csdb.setAaP1prime(aaP1prime);
                    System.out.println(aaP1);
                    System.out.println(aaP1prime);
                }
            }

            String expr7 = "<div\\s+id=\"pubmed\">[^<]+<table>[^<]+<td>[^<]+<a\\s+href=\"[^\"]+\"\\s+target=\"[^\"]+\"\\s+>"
                    + "([^<]+)";

            Pattern p7 = Pattern.compile(expr7,
                    Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
            Matcher m7 = p7.matcher(string3);
            String pmid = null;
            if (m7.find()) {
                pmid = m7.group(1);
                pmid = pmid.trim();
                pmid = pmid.replaceAll(",", ";");
                csdb.setCspmid(pmid);
                System.out.println(pmid);
            } else {
                pmid = "";
                csdb.setCspmid(pmid);
                System.out.println(pmid);
            }





            String expr10 = "(<td><font\\s+color=\"#FF0000\">)"
                    + "([^<]+)"
                    + "</font>"
                    + "([^<]+)";


            Pattern p10 = Pattern.compile(expr10,
                    Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
            Matcher m10 = p10.matcher(string3);
            String error = null;
            String errorsequence = null;
            if (m10.find()) {
                String errormsg1 = m10.group(1);
                String errormsg2 = m10.group(2);
                String errormsg3 = m10.group(3);
                error = "check sequence in Uniprot!";
                errorsequence = errormsg2 + errormsg3;
                csdb.setCserrormsg(error);
                csdb.setCscuration(errorsequence);
                System.out.println(error);
                System.out.println(errorsequence);
            } else {
                error = "";
                errorsequence = "";
                csdb.setCserrormsg(error);
                csdb.setCscuration(errorsequence);
                System.out.println(error);
                System.out.println(errorsequence);
            }

            String expr11 = "<table>[^<]+<td\\s+class=\"seq\">"
                    + "([^<]+)";
            Pattern p11 = Pattern.compile(expr11,
                    Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
            Matcher m11 = p11.matcher(string3);
            String error2 = null;
            if (m11.find()) {
                String errormsg1 = m11.group(1);
                errormsg1 = errormsg1.trim();

                if (errormsg1.startsWith("M")) {
                } else {
                    error2 = "check sequence in Uniprot!";
                    csdb.setCserrormsg(error2);
                    System.out.println(error2);
                }
            } else {
                error2 = "";
                csdb.setCserrormsg(error2);
                System.out.println(error2);
            }


            alldata.add(csdb);

        }
        
        System.out.println("-----------------");
        try {
            csvWriter = new PrintStream("PMAPCSDBp11.csv");

            csvWriter.print("PMAP Protease Name");
            csvWriter.print(",");
            csvWriter.print("Protease Name");
            csvWriter.print(",");
            csvWriter.print("Protease Accession");
            csvWriter.print(",");
            csvWriter.print("Protease EC Number");
            csvWriter.print(",");
            csvWriter.print("Protease taxonomy");
            csvWriter.print(",");
            csvWriter.print("Substrate Gene Name");
            csvWriter.print(",");
            csvWriter.print("Substrate Protein Name");
            csvWriter.print(",");
            csvWriter.print("Substrate Accession");
            csvWriter.print(",");
            csvWriter.print("Substrate Sequence");
            csvWriter.print(",");
            csvWriter.print("Substrate taxonomy");
            csvWriter.print(",");
            csvWriter.print("P1 #");
            csvWriter.print(",");
            csvWriter.print("P1' #");
            csvWriter.print(",");
            csvWriter.print("P1 aa");
            csvWriter.print(",");
            csvWriter.print("P1' aa");
            csvWriter.print(",");
            csvWriter.print("external link");
            csvWriter.print(",");
            csvWriter.print("PMID");
            csvWriter.print(",");
            csvWriter.print("Comment");
            csvWriter.print(",");
            csvWriter.print("Check Sequence");
            csvWriter.print("\n");
            for (CleavageSiteDBEntryPMAP csdb : alldata) {
                System.out.println(csdb);
                csvWriter.print(csdb.protease.getEnzymename());
                csvWriter.print(",");
                csvWriter.print("");
                csvWriter.print(",");
                csvWriter.print("");
                csvWriter.print(",");
                csvWriter.print("");
                csvWriter.print(",");
                csvWriter.print(csdb.protease.getEnzymetaxonomy());
                csvWriter.print(",");
                csvWriter.print(csdb.substrate.getSubstratename());
                csvWriter.print(",");
                csvWriter.print(csdb.substrate.getSubstrateprotname());
                csvWriter.print(",");
                csvWriter.print(csdb.substrate.getSubstrateacessionlist());
                csvWriter.print(",");
                csvWriter.print("");
                csvWriter.print(",");
                csvWriter.print("9606");
                csvWriter.print(",");
                csvWriter.print(csdb.getP1());
                csvWriter.print(",");
                csvWriter.print(csdb.getP1prime());
                csvWriter.print(",");
                csvWriter.print(csdb.getAaP1());
                csvWriter.print(",");
                csvWriter.print(csdb.getAaP1prime());
                csvWriter.print(",");
                csvWriter.print(csdb.getCsurl());
                csvWriter.print(",");
                csvWriter.print(csdb.getCspmid());
                csvWriter.print(",");
                csvWriter.print(csdb.getCserrormsg());
                csvWriter.print(",");
                csvWriter.print(csdb.getCscuration());
                csvWriter.print("\n");
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetUrlBIS.class.getName()).log(Level.SEVERE, null, ex);
        }



        csvWriter.close();

//        BORDEL I DONT NEED ANYMORE
//            String expr3 = "</label>[^<]+</td>[^<]+<td><a\\s+href\\s+=\\s+\"[^\"]+\"\\s+target=\"[^\"]+\">";
//            StringBuilder sbd3 = new StringBuilder();
//            Pattern p3 = Pattern.compile(expr3,
//                Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
//        Matcher m3 = p3.matcher(string2);
//        
//
//        while (m3.find()) {
//            String string3 = m3.replaceAll("</label>"+"\t"+"</td>"+"\t"+"<td>");
//            sbd3.append(string3);
//        }
//        
//        String string3 = sbd3.toString();
//        
//        String expr4 = "<td><br>";
//        StringBuilder sbd4 = new StringBuilder();
//            Pattern p4 = Pattern.compile(expr4,
//                Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
//        Matcher m4 = p4.matcher(string3);
//        
//
//        while (m4.find()) {
//            String string4 = m4.replaceAll("<td>"+"?"+"<br>");
//            sbd4.append(string4);
//        }    
//        
//        String string4 = sbd4.toString();
////        
//        String expr5 = "<a\\s+href\\s+=\"[^\"]+\"\\s+target=\"[^\"]+\">";
//        StringBuilder sbd5 = new StringBuilder();
//            Pattern p5 = Pattern.compile(expr5,
//                Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
//        Matcher m5 = p5.matcher(string4);
//        
//
//        while (m5.find()) {
//            String string5 = m5.replaceAll("");
//            sbd5.append(string5);
//            
//        }   
//        
//        
//      
//            System.out.println(sbd5.toString());
        //System.out.println(sbd2.toString());
//        String expr = "(<td><a)(.*?[^>]+)";
//       
//        
//       
//        Pattern p = Pattern.compile(expr,
//                Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
//        Matcher m = p.matcher(string);
//        StringBuffer sb = new StringBuffer(string.length());
//
//        while (m.find()) {
//            String bordel = m.group(1);
//            String bordel2 = m.group(2);
//            System.out.println(bordel2);
//            m.appendReplacement(sb, bordel);
//            
//            
//             }
//            System.out.println(sb.toString());
//            //m.appendTail(sb2);
//            
//    
    }  // end of main

    public static void main(String[] args) {
        GetUrlBIS JuliePMAP = new GetUrlBIS();
    }
} 
