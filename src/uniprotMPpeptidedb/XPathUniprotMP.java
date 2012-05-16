/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uniprotMPpeptidedb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author julieklein
 */
public class XPathUniprotMP {
     public NodeList getNodeListByXPath (String xpathQuery, Document xmlDoc){
          NodeList r = null;
        try {
           
            r = (NodeList) XPathFactory.newInstance().newXPath().evaluate(xpathQuery, xmlDoc, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XPathUniprotMP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
         
     }
    
}
