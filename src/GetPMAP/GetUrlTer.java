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
import org.w3c.dom.Document;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * @author julieklein
 */
public class GetUrlTer {

    private void getCleavagesitePosition(Matcher patternCleavagesitePosition, CleavageSiteDBEntryPMAP csdb) throws NumberFormatException {
        while (patternCleavagesitePosition.find()) {
            String position = patternCleavagesitePosition.group(1);
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
//                    String P1prime = positionSplit[1];
                int intP1 = Integer.parseInt(P1);
                csdb.setP1(intP1);
//                    int intP1prime = Integer.parseInt(P1prime);
                int intP1prime = intP1 + 1;
                csdb.setP1prime(intP1prime);
                System.out.println(intP1);
                System.out.println(intP1prime);
            }
        }
    }

    private void getCleavagesiteSequence(Matcher patternCleavagesiteSequence, CleavageSiteDBEntryPMAP csdb) {
        while (patternCleavagesiteSequence.find()) {
            String sequence = patternCleavagesiteSequence.group(1);
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
                csdb.setCleavagesiteseaquence("no information");
                System.out.println("no information");

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
                sequence = sequence.replaceAll("-", "");
                csdb.setCleavagesiteseaquence(sequence);
                System.out.println(sequence);
            }
        }
    }

    private void getEnzymeInformation(BufferedReader bReader, String enzymeName, ProteaseDBEntryPMAP pdb) throws IOException {
        String line;
        while ((line = bReader.readLine()) != null) {
            String splitarray[] = line.split("\t");
            String naturallanguage = splitarray[0];
            naturallanguage = naturallanguage.replaceAll("\"", "");
            if (naturallanguage.equals(enzymeName)) {
                String enzymeSymbol = splitarray[1];
                enzymeSymbol = enzymeSymbol.replaceAll("sept-0", "SEPT");
                String enzymeUniprot = splitarray[2];
                String enzymeBrenda = splitarray[3];
                pdb.setEnzymesymbol(enzymeSymbol);
                pdb.setEnzymeUniprot(enzymeUniprot);
                pdb.setEnzymeBrenda(enzymeBrenda);
                System.out.println(enzymeSymbol);
                System.out.println(enzymeUniprot);
                System.out.println(enzymeBrenda);
            }
        }
    }

    private String getEnzymeTaxon(Matcher patternEnzymeTaxon) {
        String enzymeTaxon = null;
        if (patternEnzymeTaxon.find()) {
            enzymeTaxon = patternEnzymeTaxon.group(2);
            enzymeTaxon = enzymeTaxon.trim();
        } else {
            enzymeTaxon = "n.d.";
        }
        return enzymeTaxon;
    }

    private void getErrorUnmatched(Matcher patternErrorUnmatched, CleavageSiteDBEntryPMAP csdb) {
        if (patternErrorUnmatched.find()) {
            String errormunmatched = patternErrorUnmatched.group(1);
            String error = "Unmatched cleavage site";
            csdb.setCserrormsg(error);
            System.out.println(error);

        } else {
            String error = "-";
            csdb.setCserrormsg(error);
            System.out.println(error);
        }

//            String expr11 = "<table>[^<]+<td\\s+class=\"seq\">"
//                    + "([^<]+)";
//            Pattern p11 = Pattern.compile(expr11,
//                    Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
//            Matcher m11 = p11.matcher(pmapentry);
//            String error2 = null;
//            if (m11.find()) {
//                String errormsg1 = m11.group(1);
//                errormsg1 = errormsg1.trim();
//
//                if (errormsg1.startsWith("M")) {
//                } else {
//                    error2 = "check sequence in Uniprot!";
//                    csdb.setCserrormsg(error2);
//                    System.out.println(error2);
//                }
//            } else {
//                error2 = "";
//                csdb.setCserrormsg(error2);
//                System.out.println(error2);
//            }
    }

    private StringBuilder getHtmlcontent(URL u) throws IOException {
        InputStream is = null;
        DataInputStream dis;
        String s = null;
        StringBuilder htmlcontent = new StringBuilder();

        is = u.openStream();
        dis = new DataInputStream(new BufferedInputStream(is));
        while ((s = dis.readLine()) != null) {
            s = s.replaceAll("\\?", "");
            htmlcontent.append(s);
        }
        is.close();
        return htmlcontent;
    }

    private Matcher getPatternmatcher(String expression, String string) {
        Pattern p = Pattern.compile(expression,
                Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);
        Matcher matcher = p.matcher(string);
        return matcher;
    }

    private void getPmid(Matcher patternPmid, CleavageSiteDBEntryPMAP csdb) {
        if (patternPmid.find()) {
            String pmid = patternPmid.group(1);
            pmid = pmid.trim();
            pmid = pmid.replaceAll(",", ";");
            csdb.setCspmid(pmid);
            System.out.println(pmid);
        } else {
            String pmid = "-";
            csdb.setCspmid(pmid);
            System.out.println(pmid);
        }
    }

    private void getProteinNameSymbolAccession(Matcher patternProteinName, SubstrateDBEntryPMAP sdb, String pmapentry) throws IOException {
        if (patternProteinName.find()) {
            String proteinname = patternProteinName.group(1);
            proteinname = proteinname.trim();
            proteinname = proteinname.replaceAll(",", "");
            sdb.setSubstrateprotname(proteinname);
            System.out.println(proteinname);

            BufferedReader bReader = createBufferedreader("//Users/julieklein/Desktop/ProteasiX/PMAPSubstrateLibrairybis.txt");
            String line;
            while ((line = bReader.readLine()) != null) {
                String splitarray[] = line.split("\t");
                String naturallanguage = splitarray[1];
                naturallanguage = naturallanguage.replaceAll("\"", "");
                if (naturallanguage.equals(proteinname)) {
                    String proteinsymbol = splitarray[0];
                    proteinsymbol = proteinsymbol.replaceAll("sept-0", "SEPT");
                    String proteinaccession = splitarray[2];
                    System.out.println(proteinsymbol);
                    System.out.println(proteinaccession);
                    sdb.setSubstratename(proteinsymbol);
                    sdb.setSubstrateacessionlist(proteinaccession);
                }
            }

        } else {

            Matcher patternProteinSymbol = getPatternmatcher("Substrate[^<]+</th>[^<]+<td>[^<]+<table>[^<]+<tr>[^<]+<th\\s+class=\"th3\">Definition:</th>[^<]+<td><b>"
                    + "[^<]+"
                    + "</b></td>[^<]+<tr>[^<]+<th\\s+class=\"th3\">Symbol:</th>"
                    + "([^<]+<td><b>)?([^<]+)", pmapentry);
            getProteinSymbol(patternProteinSymbol, sdb);
            Matcher patternProteinAccession = getPatternmatcher("UniProt\\s+Accession:</th>[^<]+<td><a\\s+href\\s+=\\s+\"[^\"]+\"\\s+target=\"[^\"]+\">"
                    + "([^<]+)", pmapentry);
            getProteinAccession(patternProteinAccession, sdb);
        }
//else{
//                    System.out.println("n.d");
//                    System.out.println("n.d");
//                    sdb.setSubstratename("n.d");
//                    sdb.setSubstrateacessionlist("n.d");
//                    
//                }
    }

    private void getProteinAccession(Matcher patternProteinAccession, SubstrateDBEntryPMAP sdb) {
        if (patternProteinAccession.find()) {
            String accession = patternProteinAccession.group(1);
            accession = accession.trim();
            sdb.setSubstrateacessionlist(accession);
            System.out.println(accession);
        } else {
            String accession = "n.d.";
            sdb.setSubstrateacessionlist(accession);
            System.out.println(accession);
        }
    }

    private BufferedReader createBufferedreader(String datafilename) throws FileNotFoundException {
        BufferedReader bReader = new BufferedReader(
                new FileReader(datafilename));
        return bReader;

    }

    private void getProteinSymbol(Matcher patternProteinSymbol, SubstrateDBEntryPMAP sdb) {

        if (patternProteinSymbol.find()) {
            String proteinsymbol = patternProteinSymbol.group(2);
            sdb.setSubstratename(proteinsymbol);
            System.out.println(proteinsymbol);
        } else {
            String proteinsymbol = "n.d.";
            sdb.setSubstratename(proteinsymbol);
            System.out.println(proteinsymbol);
        }
    }

    private String putSplittedhtmlintostringbuilder(Matcher splithtml) {
        StringBuilder sbd = new StringBuilder();
        while (splithtml.find()) {
            String entry = splithtml.group(1);
            entry = entry + "\n" + "******************************" + "\n";
            //System.out.println(entry);
            sbd.append(entry);
        }
        String splittedentry = sbd.toString();
        return splittedentry;
    }

    public GetUrlTer() throws IOException {
        /**
         * @param args the command line arguments
         */
//       
        PrintStream csvWriter = null;
        LinkedList<CleavageSiteDBEntryPMAP> alldata = new LinkedList<CleavageSiteDBEntryPMAP>();
//
//       
        String htmlcontent = getHtmlcontent(new URL("file:///Users/julieklein/Desktop/ProteasiX/PMAP/MARS2012/search_result_1.html")).toString();


        Matcher splithtml = getPatternmatcher("(<input\\s+id=\"ballot.*?>Detail</a></td>)", htmlcontent);
        String htmlsplitted = putSplittedhtmlintostringbuilder(splithtml);
        Matcher retrievepmapentryid = getPatternmatcher("<td><a\\s+href=\""
                + "([^\"]+)"
                + "\"[^>]*>", htmlsplitted);

        while (retrievepmapentryid.find()) {
            String url = retrievepmapentryid.group(1);

            url = "http://cutdb.burnham.org" + url;



            String pmapentry = getHtmlcontent(new URL(url)).toString();
            Matcher patternEnzymeTaxon = getPatternmatcher("<div\\s+id=\"protdata\">[^<]+<table>[^<]+<tr>[^<]+<th\\s+class=\"th3\">Definition:</th>"
                    + "([^<]+<td><b>[^<]+</b></td>)?"
                    + "[^<]+</tr>[^<]+<tr>[^<]+<th\\s+class=\"th3\">Organism:</th>[^<]+<td><a\\s+href\\s+=\\s+\"[^\"]+\"\\s+target=\"[^\"]+\">"
                    + "([^<]+)", pmapentry);
            String enzymeTaxon = getEnzymeTaxon(patternEnzymeTaxon);



            if (!(enzymeTaxon.equalsIgnoreCase("Homo Sapiens")) && !(enzymeTaxon.equalsIgnoreCase("Rattus Norvegicus")) && !(enzymeTaxon.equalsIgnoreCase("Mus Musculus"))) {
                continue;
            } else {
                CleavageSiteDBEntryPMAP csdb = new CleavageSiteDBEntryPMAP();
                csdb.setCsurl(url);
                System.out.println("\n" + "******************************" + url);

                ProteaseDBEntryPMAP pdb = new ProteaseDBEntryPMAP();
                csdb.setProtease(pdb);
                pdb.setEnzymetaxonomy(enzymeTaxon);
                System.out.println(enzymeTaxon);

                Matcher patternEnzymeName = getPatternmatcher("<div\\s+id=\"protdata\">[^<]+<table>[^<]+<tr>[^<]+<th\\s+class=\"th3\">Definition:</th>[^<]+<td><b>"
                        + "([^<]+)", pmapentry);

                if (patternEnzymeName.find()) {
                    String enzymeName = patternEnzymeName.group(1);
                    enzymeName = enzymeName.trim();
                    enzymeName = enzymeName.replaceAll(",", "");
                    pdb.setEnzymename(enzymeName);
                    System.out.println(enzymeName);
                    if (enzymeTaxon.equalsIgnoreCase("Homo Sapiens")) {
                        BufferedReader bReader = createBufferedreader("//Users/julieklein/Desktop/ProteasiX/PMAPProteaseHSALibrairy.txt");
                        getEnzymeInformation(bReader, enzymeName, pdb);

                    } else {
                        if (enzymeTaxon.equalsIgnoreCase("Rattus Novegicus")) {
                            BufferedReader bReader = createBufferedreader("//Users/julieklein/Desktop/ProteasiX/PMAPProteaseRNOLibrairy.txt");
                            getEnzymeInformation(bReader, enzymeName, pdb);
                        } else {
                            if (enzymeTaxon.equalsIgnoreCase("Mus Musculus")) {
                                BufferedReader bReader = createBufferedreader("//Users/julieklein/Desktop/ProteasiX/PMAPProteaseMMULibrairy.txt");
                                getEnzymeInformation(bReader, enzymeName, pdb);
                            }
                        }               
                    }
                } else {
                    String enzymename = "n.d.";
                    String enzymeSymbol = "n.d.";
                    String enzymeUniprot = "n.d";
                    String enzymeBrenda = "n.d.";
                    pdb.setEnzymename(enzymename);
                    pdb.setEnzymesymbol(enzymeSymbol);
                    pdb.setEnzymeUniprot(enzymeUniprot);
                    pdb.setEnzymeBrenda(enzymeBrenda);
                    System.out.println(enzymename);
                    System.out.println(enzymeSymbol);
                    System.out.println(enzymeUniprot);
                    System.out.println(enzymeBrenda);
                }





                Matcher patternProteinName = getPatternmatcher("Substrate[^<]+</th>[^<]+<td>[^<]+<table>[^<]+<tr>[^<]+<th\\s+class=\"th3\">Definition:</th>[^<]+<td><b>"
                        + "([^<]+)", pmapentry);
                SubstrateDBEntryPMAP sdb = new SubstrateDBEntryPMAP();
                csdb.setSubstrate(sdb);
                getProteinNameSymbolAccession(patternProteinName, sdb, pmapentry);




                Matcher patternCleavagesitePosition = getPatternmatcher("<div\\s+id=\"cleav2\">[^<]+<table>[^<]+<th\\s+class=\"th3\">Position:</th>[^<]+<td>"
                        + "([^<]+)?", pmapentry);
                getCleavagesitePosition(patternCleavagesitePosition, csdb);

                Matcher patternCleavagesiteSequence = getPatternmatcher("<div\\s+id=\"cleav\">[^<]+<table>[^<]+<th\\s+class=\"th3\">Sequence:</td>[^<]+<td>"
                        + "([^<]+)?", pmapentry);
                getCleavagesiteSequence(patternCleavagesiteSequence, csdb);

                Matcher patternPmid = getPatternmatcher("<div\\s+id=\"pubmed\">[^<]+<table>[^<]+<td>[^<]+<a\\s+href=\"[^\"]+\"\\s+target=\"[^\"]+\"\\s+>"
                        + "([^<]+)", pmapentry);
                getPmid(patternPmid, csdb);


                Matcher patternErrorUnmatched = getPatternmatcher("<td><font\\s+color=\"#FF0000\">"
                        + "(\\*Unmatched)", pmapentry);
                getErrorUnmatched(patternErrorUnmatched, csdb);


                alldata.add(csdb);
            }
        }

        System.out.println("-----------------");
        try {
            csvWriter = new PrintStream("PMAPCSDBp3.csv");


            for (CleavageSiteDBEntryPMAP csdb : alldata) {
                System.out.println(csdb);
                csvWriter.print(csdb.protease.getEnzymename());
                csvWriter.print(",");
                csvWriter.print(csdb.protease.getEnzymesymbol());
                csvWriter.print(",");
                csvWriter.print(csdb.protease.getEnzymeUniprot());
                csvWriter.print(",");
                csvWriter.print(csdb.protease.getEnzymeBrenda());
                csvWriter.print(",");
                csvWriter.print(csdb.protease.getEnzymetaxonomy());
                csvWriter.print(",");
                csvWriter.print(csdb.substrate.getSubstrateprotname());
                csvWriter.print(",");
                csvWriter.print(csdb.substrate.getSubstratename());
                csvWriter.print(",");
                csvWriter.print(csdb.substrate.getSubstrateacessionlist());
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
                csvWriter.print(csdb.getCleavagesiteseaquence());
                csvWriter.print(",");
                csvWriter.print(csdb.getCsurl());
                csvWriter.print(",");
                csvWriter.print(csdb.getCspmid());
                csvWriter.print(",");
                csvWriter.print(csdb.getCserrormsg());
                csvWriter.print(",");
                csvWriter.print("-");
                csvWriter.print("\n");
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetUrlTer.class.getName()).log(Level.SEVERE, null, ex);
        }



        csvWriter.close();


//    
    }

    public static void main(String[] args) throws IOException {
        GetUrlTer JuliePMAP = new GetUrlTer();
    }
} 
