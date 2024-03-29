
package com.brainquizapi.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
//import com.lowagie.text.rtf.field.RtfPageNumber;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.brainquizapi.response.ResultPdfResponse;
import com.brainquizapi.util.LinkInCell;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
//import com.lowagie.text.rtf.RtfHeaderFooter;






@Service
public class EmailServiceBackuo{

	Base64.Encoder baseEncoder = Base64.getEncoder();
	
	@Value("${file.upload-dir}")
    private String logoimageLocation;
	
	@Value("${result.template-dir}")
    private String templateLocation;
	
	@Value("${email.from.id}")
    private String fromemail;
	
	@Value("${email.pwd}")
    private String emailpwd;
	/*
	 * @Autowired VerificationRequestRepository verificationReqRepository;
	 * 
	 * @Autowired UserService userService;
	 * 
	 * @Autowired BranchMasterService branchService;
	 * 
	 * @Autowired SemesterService semService;
	 * 
	 * @Autowired StreamService streamService;
	 *  
	 * @Autowired YearOfPassingService yearOfPassService;
	 * 
	 * @Autowired DocumentService documentService;
	 */

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceBackuo.class);

	//public void exportResult(HttpServletResponse response,List<ResultPdfResponse> result) throws Exception {
	public void exportResult(List<ResultPdfResponse> result,String resendEmails) throws Exception {
		logger.info("*****EmailService exportResult*****");
		StringBuffer reason = null;
		String to = "";//"scubeuser8@gmail.com";
		String logoPath = "";
		
		for(ResultPdfResponse resp: result) {
			to=resp.getEmailId();
			logoPath = resp.getLogoPath();
			logger.info("*****b4TO MAIL ID*****"+to);

			
			if(resendEmails != null && !resendEmails.isEmpty())
				to=to+","+resendEmails;
				
				
			logger.info("*****adding resendTO MAIL ID*****"+to);

			break;
		}
		
		
		//String from = "mindfulness@graone.co.in";		
		//String host = "mail.graone.co.in";
		
	//	String from = "scube.usr@gmail.com";		
	//	String host = "smtp.gmail.com";
		
		//String from = "verify@educred.co.in";
	//	String host = "mail.educred.co.in";
		
		String from = fromemail;
		String host = "smtp.gmail.com";
		
	//	Email ID :
	//	Password : Mindfulness@2022
		
		Properties properties = System.getProperties();

		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		String vmFileContent = "Dear Candidate,  \r\r" +
				  " PFA result for quiz. \r\r"
				  + "Team GraOne";
		
		String subject = "GraOne Mindfullness Assessment";
		
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {

			//	return new PasswordAuthentication("verify@educred.co.in", "EduCred$2021$");
				//return new PasswordAuthentication("mindfulness@graone.co.in", "DASA47@gra1");
			//	return new PasswordAuthentication("scube.usr@gmail.com", "scube@1234");
				
//	                return new PasswordAuthentication("resolution@educred.co.in", "EduCred$2021$");
				
				return new PasswordAuthentication(fromemail, emailpwd);

			}

		});
		
		ByteArrayOutputStream outputStream = null;
		// Used to debug SMTP issues
		session.setDebug(true);
		try {
			
			MimeMessage mimeMessage = new MimeMessage(session);

			MimeBodyPart textBodyPart = new MimeBodyPart();
			
			textBodyPart.setText(vmFileContent);
			outputStream = new ByteArrayOutputStream();
			
			writeResultPdf(outputStream, result);
			
			byte[] bytes = outputStream.toByteArray();

			// construct the pdf body part
			DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			
			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);
			
			pdfBodyPart.setFileName("Result" + ".pdf");
			
			mimeMessage.saveChanges();

			Message message = new MimeMessage(session);
			BodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			message.setFrom(new InternetAddress(from));
			
			String[] recipientList = to.split(",");
			String[] recipientArr = new String[recipientList.length];
			for (String recipient : recipientList) {
				  message.addRecipients(Message.RecipientType.TO,
		                    InternetAddress.parse(recipient));
			}
			
			
          
            
            messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(dataSource));
			messageBodyPart.setFileName("Result" + ".pdf");
			multipart.addBodyPart(textBodyPart);
			multipart.addBodyPart(messageBodyPart);
//			multipart.addBodyPart(imagePart);
			message.setContent(multipart);
			
			InternetAddress iaSender = new InternetAddress(from);
		//	InternetAddress iaRecipient = new InternetAddress(to);

			//String[] recipientList = to.split(",");
			InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
			int counter = 0;
			for (String recipient : recipientList) {
			    recipientAddress[counter] = new InternetAddress(recipient.trim());
			    counter++;
			}
			
			
			
			// construct the mime message
//               MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setSender(iaSender);
			message.setSubject(subject);
			mimeMessage.setRecipients(Message.RecipientType.TO,recipientAddress );//iaRecipient
			mimeMessage.setContent(mimeMultipart);

			// send off the email

			System.out.println("sending...");
			Transport.send(message);
//	            Transport.send(message);
			System.out.println("Sent message successfully....");
			
		}catch (Exception ex) {       //MessagingException

			//throw new RuntimeException(e);
			StringBuffer exception = new StringBuffer(ex.getMessage().toString());
			  
            if (exception.indexOf("ConnectException") >= 0)      // connection problem.
            {
                reason = new StringBuffer(" Unable to Connect Mail server");
            }
            else if (exception.indexOf("SendFailedException") >= 0)      // Wrong To Address 
            {
                reason = new StringBuffer("Wrong To Mail address");
            }
            else if (exception.indexOf("FileNotFoundException") >= 0)    //File Not Found at Specified Location
            {
                reason = new StringBuffer("File Not Found at Specific location");                   
            }
            else        // Email has not been sent.
            {
                reason = new StringBuffer("Email has not been sent."+ex.toString());
            }
			
    		logger.info("*****EmailService exception*****"+reason);

		}
		
	}
	
	private void writeResultPdf(ByteArrayOutputStream outputStream,List<ResultPdfResponse> result) throws Exception {
		
		logger.info("writing result PDF--->");
		
		try {
			String stuName="";
			String testName="";
			String testCode="";
			String subDate="";
			String colors="";
			String logoPath = "";
			String url = "";
			
			for(ResultPdfResponse resp: result) {
				stuName = resp.getStudentName();
				testName = resp.getTestName();
				testCode = resp.getTestCode();
				subDate = resp.getSubDate();
				logoPath = resp.getLogoPath();
				url=resp.getUrl();
				break;
			}
			
			Document document = new Document(PageSize.A4, 40, 40, 50, 120);
			
//			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Rectagled.pdf"));
			
			// Set all required fonts here with appropriate names
			Font fontBold15 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			fontBold15.setSize(75);
			fontBold15.setColor(Color.BLACK);
			

			Font font12 = FontFactory.getFont(FontFactory.HELVETICA);
			font12.setSize(12);
			font12.setColor(Color.BLACK);

			Font font11 = FontFactory.getFont(FontFactory.HELVETICA);
			font11.setSize(11);
			font11.setColor(Color.BLACK);

			Font font9 = FontFactory.getFont(FontFactory.HELVETICA);
			font9.setSize(9);
			font9.setColor(Color.BLACK);
			
			Font fontBold13 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			fontBold13.setSize(13);
			fontBold13.setColor(Color.BLACK);

			PdfWriter pdfWriter=PdfWriter.getInstance(document, outputStream);
			
			Image img;
			//img = Image.getInstance(logoimageLocation+"/file/IMG_2021_09_24_16_12_34_1645430725137.jpg");
			img = Image.getInstance(this.logoimageLocation + "/file/graonelogo.png" );
		//	img = Image.getInstance("D://Asset_3.png");

			
			
			img.setAlignment(Element.ALIGN_LEFT);
			img.scaleToFit(100,200); // width, height  
			
			document.open();					
			
		
			Paragraph refNo = new Paragraph();
			refNo.setAlignment(Paragraph.ALIGN_CENTER);
			
		
			
			
			refNo.add(testName);
			refNo.setFont(fontBold15);	
			
      /*  
	
			Table tb =new Table(4);
	 tb.setWidth(100);
	             
			tb.setBorder(Rectangle.NO_BORDER);
		
            
			tb.setBackgroundColor(Color.GRAY);
                   Cell cellx= new Cell();
                cellx.setBorder(Rectangle.NO_BORDER);
                   cellx.add(img);
                   Cell cellb=new Cell();
                   cellb.add(new Paragraph(""));
                   cellb.setBorder(Rectangle.NO_BORDER);
                   Cell celly=new Cell();
                  


                   celly.setBorder(Rectangle.NO_BORDER);
                   
                  
                   celly.add(refNo);
                  
            
            Cell cellz=new Cell();
            cellz.setBorder(Rectangle.NO_BORDER);
            cellz.add(new Paragraph(""));
            tb.addCell(cellx);
            tb.addCell(cellb);
          
      tb.addCell(celly); */
          
          
        //  tb.addCell(cellz); 
         
                        PdfPTable df =new PdfPTable(4);
                        df.setWidthPercentage(100);
                       
                        PdfPCell z= new PdfPCell(img);
                        z.setBackgroundColor(Color.GRAY);
                        z.setBorder(Rectangle.NO_BORDER);
                        PdfPCell q= new PdfPCell(new Paragraph(""));
                        q.setBorder(Rectangle.NO_BORDER);
                        q.setBackgroundColor(Color.GRAY);
                        PdfPCell r= new PdfPCell(new Paragraph(refNo));
                        r.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        r.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        r.setBorder(Rectangle.NO_BORDER);
                        r.setBackgroundColor(Color.GRAY);
                        
                    
                        PdfPCell s= new PdfPCell(new Paragraph(""));
                        s.setBorder(Rectangle.NO_BORDER);
                        s.setBackgroundColor(Color.GRAY);
                        df.addCell(z);
                        df.addCell(q);
                        df.addCell(r);
                        df.addCell(s);
                        document.add(df);
		
			//document.add(tb);
			
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
		
			
	       PdfPTable heading1 = new PdfPTable(2);
	        heading1.setWidthPercentage(100);
	        heading1.setWidths(new int[] {50,50});
	        document.add(Chunk.NEWLINE);
	    	document.add(Chunk.NEWLINE);
	        PdfPCell emptyCell2= new PdfPCell(new Paragraph(""));
	       emptyCell2.setBorder(Rectangle.NO_BORDER);  
	       emptyCell2.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
	       PdfPCell emptyCell4= new PdfPCell(new Paragraph(""));
	       emptyCell4.setBorder(Rectangle.NO_BORDER);  
	       emptyCell4.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
          
	       PdfPCell emptyCell3= new PdfPCell(new Paragraph(""));
	       emptyCell3.setBorder(Rectangle.NO_BORDER); 
	       emptyCell3.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
	        PdfPCell contact = new PdfPCell(new Paragraph("Contact No:"));
	        contact.setHorizontalAlignment(Element.ALIGN_LEFT);
	        contact.setBorder(Rectangle.NO_BORDER);
	        PdfPCell emptyCell = new PdfPCell(new Paragraph(""));
	        emptyCell.setBorder(Rectangle.NO_BORDER);  
	        PdfPCell celll = new PdfPCell(new Paragraph(stuName));
	        celll.setHorizontalAlignment(Element.ALIGN_LEFT);
	        celll.setBorder(Rectangle.NO_BORDER);
	        PdfPCell email = new PdfPCell(new Paragraph("info@graone.co.in"));
	        email.setBorder(Rectangle.NO_BORDER);
	        email.setHorizontalAlignment(Element.ALIGN_LEFT);
	        heading1.addCell(emptyCell2);
	        heading1.addCell(contact); 
	        heading1.addCell(emptyCell3);
	        email.setPaddingRight(Element.ALIGN_RIGHT);
	        heading1.addCell(email);
	        heading1.addCell(emptyCell4);
	        document.add(Chunk.NEWLINE);
	        document.add(Chunk.NEWLINE);
	        heading1.addCell(celll); 
	      
	        heading1.addCell(emptyCell);
	        heading1.addCell(emptyCell);
	        heading1.addCell(emptyCell);
	        heading1.addCell(emptyCell);
	        heading1.addCell(emptyCell);
	        heading1.addCell(emptyCell);
	        heading1.addCell(emptyCell);
	        heading1.addCell(emptyCell);
	        heading1.addCell(emptyCell);
	        document.add(heading1);         
	      
		 document.add(Chunk.NEWLINE);
		  document.add(Chunk.NEWLINE);
			
	         
	     
//			Paragraph heading1 = new Paragraph();
//			heading1.setAlignment(Paragraph.ALIGN_CENTER);
//			heading1.setFont(font12);
////			heading1.setIndentationLeft(100);
//			heading1.add(stuName); // dynamic value here
//			heading1.add(Chunk.NEWLINE);
//			heading1.add(Chunk.NEWLINE);
//			
//			document.add(heading1);
			
//			heading 1 = 
//			dynamic greeting
//			Dear < Name of the registered user>,
//			first para
//			dynamic indicator text
//			
			
			Properties p = new Properties();
			//p.load(new FileInputStream("resultpdf.txt"));
            p.load(new FileInputStream(this.templateLocation + "/resultpdf.txt"));

		    String firstpara = p.getProperty("firstpara");
		    String secondpara = p.getProperty("secondpara");
		    String interpretation = p.getProperty("interpretation");
		    String disclaimer = p.getProperty("disclaimer");
		    String footer = p.getProperty("footer");
//		    key color explanations
		    String keyinfo1 = p.getProperty("keyinfo1");
		    String keyinfo2 = p.getProperty("keyinfo2");
		    String keyinfo3 = p.getProperty("keyinfo3");
//		    key colors
		    String keycolor1 = p.getProperty("keycolor1");
		    String keycolor2 = p.getProperty("keycolor2");
		    String keycolor3 = p.getProperty("keycolor3");
		    
		    
		    
		 //   logger.info(firstpara);
		//    logger.info(secondpara);
		    
//		    Paragraph userName = new Paragraph();
//		    userName.setAlignment(Paragraph.ALIGN_CENTER);
//		    userName.add("< NAME OF THE REGISTERED USER> \r");
//		    userName.setFont(headFont);
		    
//		    document.add(userName);
		    
		    
		    LineSeparator ls3 = new LineSeparator();
			ls3.setLineWidth(10f);
			ls3.setPercentage(79);
			ls3.setLineColor(Color.GRAY);
			ls3.setAlignment(Element.ALIGN_RIGHT);
			document.add(ls3);
		    document.add(Chunk.NEWLINE);
	        Paragraph greet = new Paragraph();
	        greet.setAlignment(Paragraph.ALIGN_LEFT);
	        greet.setIndentationLeft(100);
	        greet.setIndentationRight(50);
	        greet.add(stuName+" being assessed for "+testCode+" on "+subDate+"\r\n");
	        greet.setFont(font11);
	        greet.add(Chunk.NEWLINE);
	        greet.add(Chunk.NEWLINE);
	        
	        Paragraph pa = new Paragraph();
	        pa.setFont(font11);
	        pa.setIndentationLeft(100);
	        pa.setIndentationRight(50);
	        pa.add("Dear "+stuName+",");
	        pa.setAlignment(Paragraph.ALIGN_LEFT);
	        pa.add(Chunk.NEWLINE);
	        pa.add(Chunk.NEWLINE);
	        document.add(greet);
	        document.add(pa);
	        
	        Paragraph para1 = new Paragraph();
	        para1.setAlignment(Paragraph.ALIGN_LEFT);
	        para1.setFont(font11);
	        para1.setIndentationLeft(100);
	        para1.setIndentationRight(50);
	        para1.add(firstpara);
	        para1.add(Chunk.NEWLINE);
	        para1.add(Chunk.NEWLINE);
	        
	        document.add(para1);
	        
	        Paragraph keyheader = new Paragraph();
	        keyheader.setAlignment(Paragraph.ALIGN_LEFT);
	        keyheader.setFont(font11);
	        keyheader.setIndentationLeft(100);
	        keyheader.setIndentationRight(50);
	        keyheader.add("We have assessed your ability, qualitatively in term of the following <four> parameter \r\n" + 
	        		"and accordingly assessed your overall indicator."); // put in dynamic data where required
	        keyheader.add(Chunk.NEWLINE);
	        keyheader.add(Chunk.NEWLINE);
	        
	        document.add(keyheader);
	        ArrayList<String> value2 = new ArrayList<String>();
	        
          PdfPTable overallrestable= new PdfPTable(2);
	        
	        overallrestable.setWidthPercentage(50);
	        overallrestable.setWidths(new int [] {80,20}); 
	        for(ResultPdfResponse resp: result )
	        {
				
	        		value2.add(resp.getColors());
	        }
	        
	        
	        
	    
	        
	        
	        
	        
	        PdfPTable detailsTable = new PdfPTable(2);
	        detailsTable.setWidthPercentage(50);
	        detailsTable.setWidths(new int[] {80,20});
//	        detailsTable.setIndentationLeft(100);    
	        
	        for(ResultPdfResponse resp: result) {
	        	PdfPCell cell = new PdfPCell(new Paragraph(resp.getCategoryName()));
	        	PdfPCell color = new PdfPCell(new Paragraph(""));
	        	detailsTable.addCell(cell);
	        	
	        	Color coloramber = new Color(255, 192, 0);
	       
	        	
	        	if(resp.getColors().equals("red"))
	        		 color.setBackgroundColor(Color.RED);
				  else if(resp.getColors().equals("green"))
				  color.setBackgroundColor(Color.GREEN);
				  else
				  color.setBackgroundColor(coloramber);
				 
	        	
	        //	value.add(resp.getColors());
	        	detailsTable.addCell(color);
	        }
	    //    detailsTable.addCell(cell);
	        
	        PdfPCell cellz = new PdfPCell(new Paragraph("Overall assesment score / indicator"));
        	PdfPCell colorz= new PdfPCell(new Paragraph(""));
        	overallrestable.addCell(cellz);
        	Color coloramber = new Color(255, 192, 0);
        	  
  	      //  value.addAll(overallrestable);
  	        if(value2.contains("red"))
  	        colorz.setBackgroundColor(Color.RED);	
  	        else if(value2.contains("Amber"))
  	         colorz.setBackgroundColor(coloramber); 	
  	        else
  	        colorz.setBackgroundColor(Color.GREEN);
  	      detailsTable.addCell(cellz);
  	      detailsTable.addCell(colorz);
	        document.add(detailsTable);
	        
	        
	        Paragraph interp = new Paragraph();
	        interp.setFont(font11);
	        interp.setAlignment(Paragraph.ALIGN_LEFT);
	        interp.setIndentationLeft(100);
	        interp.setIndentationRight(50);
	        interp.add("Interpretation: "+interpretation);
	        interp.add(Chunk.NEWLINE);
	        interp.add(Chunk.NEWLINE);
	        
	        document.add(interp);
	        
	        PdfPTable scoreKey = new PdfPTable(3);
	        scoreKey.setWidthPercentage(60);
	        scoreKey.setWidths(new int[] {10,20,30});
	        
//	        Found index of seperator
	        int index1 = keycolor1.indexOf('-');
	        int index2 = keycolor2.indexOf('-');
	        int index3 = keycolor3.indexOf('-');
	        
//	        Found color name below
	        String color1 = keycolor1.substring(0,index1);
	        String color2 = keycolor2.substring(0,index2);
	        String color3 = keycolor3.substring(0,index3);
	        
//	        Found explanation of color below
	        String exp1 = keycolor1.substring(index1+1, keycolor1.length());
	        String exp2 = keycolor2.substring(index2+1, keycolor2.length());
	        String exp3 = keycolor3.substring(index3+1, keycolor3.length());
	        Color coloramber2 = new Color(255, 192, 0);
	        
	        PdfPCell cell1 = new PdfPCell(new Paragraph(""));
	        if(color1.equalsIgnoreCase("Red")) {
	        	cell1.setBackgroundColor(Color.RED);
	        }else if(color1.equalsIgnoreCase("Amber")) {
	        	cell1.setBackgroundColor(coloramber2);
	        }else if(color1.equalsIgnoreCase("Green")) {
	        	cell1.setBackgroundColor(Color.GREEN);
	        }
	        PdfPCell col1 = new PdfPCell(new Paragraph(color1));
	        col1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        PdfPCell expl1 = new PdfPCell(new Paragraph(exp1));
	        expl1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        PdfPCell cell2 = new PdfPCell(new Paragraph(""));
	        if(color2.equalsIgnoreCase("Red")) {
	        	cell2.setBackgroundColor(Color.RED);
	        }else if(color2.equalsIgnoreCase("Amber")) {
	        	cell2.setBackgroundColor(coloramber2);
	        }else if(color2.equalsIgnoreCase("Green")) {
	        	cell2.setBackgroundColor(Color.GREEN);
	        }
	        PdfPCell col2 = new PdfPCell(new Paragraph(color2));
	        col2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        PdfPCell expl2 = new PdfPCell(new Paragraph(exp2));
	        expl2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        PdfPCell cell3 = new PdfPCell(new Paragraph(""));
	        
	        if(color3.equalsIgnoreCase("Red")) {
	        	cell3.setBackgroundColor(Color.RED);
	        }else if(color3.equalsIgnoreCase("Amber")) {
	        	cell3.setBackgroundColor(coloramber2);
	        }else if(color3.equalsIgnoreCase("Green")) {
	        	cell3.setBackgroundColor(Color.GREEN);
	        }
	        PdfPCell col3 = new PdfPCell(new Paragraph(color3));
	        col3.setHorizontalAlignment(Element.ALIGN_CENTER);
	        PdfPCell expl3 = new PdfPCell(new Paragraph(exp3));
	        expl3.setHorizontalAlignment(Element.ALIGN_CENTER);
	        
	        scoreKey.addCell(cell1);
	        scoreKey.addCell(col1);
	        scoreKey.addCell(expl1);
	        scoreKey.addCell(cell2);
	        scoreKey.addCell(col2);
	        scoreKey.addCell(expl2);
	        scoreKey.addCell(cell3);
	        scoreKey.addCell(col3);
	        scoreKey.addCell(expl3);
	        
	        document.add(scoreKey);
	        
	        
//	        PdfPCell cell1 = new PdfPCell(new Paragraph(""));
//	        cell1.setBackgroundColor(Color.GREEN);
//	        PdfPCell cell2 = new PdfPCell(new Paragraph("Green"));
//	        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        PdfPCell cell3 = new PdfPCell(new Paragraph("Normal Ability"));
//	        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        PdfPCell cell4 = new PdfPCell(new Paragraph(""));
//	        cell4.setBackgroundColor(Color.YELLOW);
//	        PdfPCell cell5 = new PdfPCell(new Paragraph("Amber"));
//	        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        PdfPCell cell6 = new PdfPCell(new Paragraph("Moderately Impacted Ability"));
//	        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        PdfPCell cell7 = new PdfPCell(new Paragraph(""));
//	        cell7.setBackgroundColor(Color.RED);
//	        PdfPCell cell8 = new PdfPCell(new Paragraph("Red"));
//	        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        PdfPCell cell9 = new PdfPCell(new Paragraph("Severely Impacted Ability"));
//	        cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        
//	        scoreKey.addCell(cell1);
//	        scoreKey.addCell(cell2);
//	        scoreKey.addCell(cell3);
//	        scoreKey.addCell(cell4);
//	        scoreKey.addCell(cell5);
//	        scoreKey.addCell(cell6);
//	        scoreKey.addCell(cell7);
//	        scoreKey.addCell(cell8);
//	        scoreKey.addCell(cell9);
//	        
//	        document.add(scoreKey);
	        
//	        PdfContentByte canvas = writer.getDirectContent();
//	        canvas.saveState();
//	        canvas.setColorStroke(Color.black);
//	        canvas.rectangle(10,10,10,10);
//	        canvas.stroke();
//	        canvas.restoreState();
	        
	        Paragraph para2 = new Paragraph();
	        para2.setFont(font11);
	        para2.setAlignment(Paragraph.ALIGN_LEFT);
	        para2.setIndentationLeft(100);
	        para2.setIndentationRight(50);
	        para2.add(secondpara);
	        para2.add(Chunk.NEWLINE);
	        para2.add(Chunk.NEWLINE);
	        
	        document.add(para2);
	        
	        Paragraph disclaim = new Paragraph();
	        disclaim.setFont(font11);
	        disclaim.setAlignment(Paragraph.ALIGN_LEFT);
	        disclaim.setIndentationLeft(100);
	        disclaim.setIndentationRight(50);
	        disclaim.add("Disclaimer: "+disclaimer);
	        disclaim.add(Chunk.NEWLINE);
	        disclaim.add(Chunk.NEWLINE);
	        
	        document.add(disclaim);
	        LineSeparator ls4 = new LineSeparator();
			ls4.setLineWidth(10f);
			ls4.setPercentage(80);
			ls4.setLineColor(Color.GRAY);
			ls4.setAlignment(Element.ALIGN_RIGHT);
			 document.add(ls4);
	        
	        
	        
//	        HeaderFooter footerr = new HeaderFooter( new Phrase(footer, footerFont9),false); 
//	        footerr.setAlignment(Element.ALIGN_CENTER); 
//	   		footerr.setBorder(Rectangle.NO_BORDER); 
//	   		document.setFooter(footerr);
	   		
	/* 		HeaderFooter foo =    new HeaderFooter( new Phrase(footer, font9), false);
            foo.setAlignment(Element.ALIGN_CENTER);
            foo.setBorder(Rectangle.NO_BORDER);
            document.setFooter(foo);    */
	        
	        Paragraph keyinfoo = new Paragraph();
	        keyinfoo.setFont(font11);
	        keyinfoo.setAlignment(Paragraph.ALIGN_LEFT);
	        keyinfoo.setIndentationLeft(100);
	        keyinfoo.setIndentationRight(50);
	        keyinfoo.add("Legend: \r\n"+keyinfo1+"\r\n"+keyinfo2+"\r\n"+keyinfo3);
	        
	        document.add(keyinfoo);
	        document.add(Chunk.NEWLINE);
	        document.add(Chunk.NEWLINE);  
	        
	        Paragraph footernote = new Paragraph();
	        footernote.setFont(font11);
	        footernote.setAlignment(Paragraph.ALIGN_LEFT);
	        footernote.setIndentationLeft(100);
	        footernote.setIndentationRight(50);
	        footernote.add(footer);
	        
	        document.add(footernote);
	        document.add(Chunk.NEWLINE);
	        document.add(Chunk.NEWLINE);  
	        
	        //test table code
			
			/*
			 * Image img11; img11 = Image.getInstance("D://Asset_3.png");
			 * img11.setAlignment(Element.ALIGN_LEFT); img11.scaleToFit(10,20); // width,
			 * height
			 * 
			 * PdfPTable test =new PdfPTable(1); df.setWidthPercentage(100);
			 * 
			 * PdfPCell tst= new PdfPCell(img11); z.setBackgroundColor(Color.GRAY);
			 * z.setBorder(Rectangle.NO_BORDER);
			 * 
			 * test.addCell(tst); document.add(test);
			 */
			 
            //
            
            
            //
            Image img111;
			//img111 = Image.getInstance("D://Asset_3.png");
			img111 = Image.getInstance(this.logoimageLocation + "/"+logoPath);
			
			img111.setAlignment(Element.ALIGN_LEFT);
			img111.scaleToFit(80,100); // width, height  
			//img111.setUrl(www.google.com);
			
	        PdfPTable test1 =new PdfPTable(1);
	        test1.setWidthPercentage(100);
           
	        
			/*
			 * PdfPCell tst1= new PdfPCell(img111); // tst1.setBackgroundColor(Color.GRAY);
			 * tst1.setBorder(Rectangle.NO_BORDER); Paragraph paragraph = new
			 * Paragraph("Being aware about the mindfulness is good but it is only the first step. Join our partner network and interact with their experts \r\n"
			 * +
			 * "panel to explore mind and body holistically and be happier. Visit our partner site by clicking on the logo below"
			 * ); tst1.addElement(paragraph); test1.addCell(tst1);
			 */
	        
            PdfPCell tst2= new PdfPCell(img111);
            tst2.setBackgroundColor(Color.GRAY);
            tst2.setBorder(Rectangle.NO_BORDER);
            tst2.setCellEvent(new LinkInCell(url));

            
            test1.addCell(tst2);
           
            //document.add(test);
			Rectangle page = document.getPageSize(); 
 
			test1.setTotalWidth(page.getWidth() -document.leftMargin() - document.rightMargin()); 
			test1.writeSelectedRows(0,-1, document.leftMargin(), document.bottomMargin(), pdfWriter.getDirectContent());
		 
            
            
            //
            
            
            
	         
			/*
			 * Rectangle page = document.getPageSize(); PdfPTable foot = new PdfPTable(1);
			 * Image image = Image.getInstance("D://Asset_3.png");
			 * image.scaleToFit(100,100); foot.addCell(image);
			 * foot.setTotalWidth(page.getWidth() -document.leftMargin() -
			 * document.rightMargin()); foot.writeSelectedRows(0,-1, document.leftMargin(),
			 * document.bottomMargin(), pdfWriter.getDirectContent());
			 */
	       
			document.close();
             
			
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
		}

	public float getMyHeaderHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
}
