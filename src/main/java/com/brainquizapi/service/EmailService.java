
package com.brainquizapi.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.StyleConstants.FontConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.brainquizapi.response.ResultPdfResponse;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.ElementPropertyContainer;  
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.Canvas;

@SuppressWarnings("unused")
@Service
public class EmailService {

	private static PdfDocument pdfDoc;
	private static Image image;
	
	
Base64.Encoder baseEncoder = Base64.getEncoder();
	
	@Value("${file.upload-dir}")
    private String logoimageLocation;
	
	@Value("${result.template-dir}")
    private String templateLocation;
	
	@Value("${email.from.id}")
    private String fromemail;
	
	@Value("${email.pwd}")	
    private String emailpwd;
	
	@Value("${email.host}")
    private String emailhost;
	
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

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	//public void exportResult(HttpServletResponse response,List<ResultPdfResponse> result) throws Exception {
	public void exportResult(List<ResultPdfResponse> result,String resendEmails) throws Exception {
		logger.info("*****EmailService exportResult*****");
		StringBuffer reason = null;
		String to = "";//"scubeuser8@gmail.com";
		
		for(ResultPdfResponse resp: result) {
			to=resp.getEmailId();
			
			logger.info("*****b4TO MAIL ID*****"+to);

			
			if(resendEmails != null && !resendEmails.isEmpty())
				to=to+","+resendEmails;
				
				
			logger.info("*****adding resendTO MAIL ID*****"+to);

			break;
		}
		
		
	//	String from = fromemail;
		
		String host = emailhost;
		
		Properties properties = System.getProperties();

		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		String vmFileContent = "Dear Candidate,  \r\r" +
				  " PFA result for quiz. \r\r"
				  + "Team Brainquiz";
		
		String subject = "BrainQuiz Result";
		
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {

//				return new PasswordAuthentication("verify@educred.co.in", "EduCred$2021$");
				return new PasswordAuthentication(fromemail, emailpwd);
//	                return new PasswordAuthentication("resolution@educred.co.in", "EduCred$2021$");

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
			
			pdfBodyPart.setFileName("temp" + ".pdf");
			
			mimeMessage.saveChanges();

			Message message = new MimeMessage(session);
			BodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			message.setFrom(new InternetAddress(fromemail));
			
			String[] recipientList = to.split(",");
			String[] recipientArr = new String[recipientList.length];
			for (String recipient : recipientList) {
				  message.addRecipients(Message.RecipientType.TO,
		                    InternetAddress.parse(recipient));
			}
			
			
          
            
            messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(dataSource));
			messageBodyPart.setFileName("AssessmentResult" + ".pdf");
			multipart.addBodyPart(textBodyPart);
			multipart.addBodyPart(messageBodyPart);
//			multipart.addBodyPart(imagePart);
			message.setContent(multipart);
			
			InternetAddress iaSender = new InternetAddress(fromemail);
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

			ex.printStackTrace();
			
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
                reason = new StringBuffer("Email has not been sent.");
            }
			
    		logger.info("*****EmailService exception*****"+reason);

		}
		
	}
	
	

	private void writeResultPdf(ByteArrayOutputStream outputStream,List<ResultPdfResponse> result) throws Exception {
		
		try {
			
			//System.out.println("hello"+result.size());
			
			String stuName="";
			String testName="";
			String testCode="";
			String subDate="";
			String colors="";
		
			
			for(ResultPdfResponse resp: result) {
				stuName = resp.getStudentName();
				testName = resp.getTestName();
				testCode = resp.getTestCode();
				subDate = resp.getSubDate();
				
				break;
			}

			
			Properties p = new Properties();
			p.load(new FileInputStream("resultpdf.txt"));
			String firstpara1 = p.getProperty("firstpara1");
		    String firstpara = p.getProperty("firstpara");
		    String secondpara = p.getProperty("secondpara");
		    String secondpara1 = p.getProperty("secondpara1");
		    String thirdpara = p.getProperty("thirdpara");
		    String thirdpara1 = p.getProperty("thirdpara1");
		    String interpretation = p.getProperty("interpretation");
		    String disclaimer = p.getProperty("disclaimer");
		    String footer = p.getProperty("footer");
//		    key color explanations
		    String keyinfo1 = p.getProperty("keyinfo1");
		    String keyinfo2 = p.getProperty("keyinfo2");
		    String keyinfo3 = p.getProperty("keyinfo3");
//		    key colors
		    String keycolor1 = p.getProperty("keycolor1");
		    String keycolor11 = p.getProperty("keycolor11");
		    String keycolor2 = p.getProperty("keycolor2");
		    String keycolor22 = p.getProperty("keycolor22");
		    String keycolor3 = p.getProperty("keycolor3");
		    String keycolor33 = p.getProperty("keycolor33");
		    String fourthpara = p.getProperty("fourthpara");
		    String fourthpara1 = p.getProperty("fourthpara1");
		    String fourthpara2 = p.getProperty("fourthpara2");
		    String fourthpara3 = p.getProperty("fourthpara3");
		    String fourthpara4 = p.getProperty("fourthpara4");
		    String fourthpara5 = p.getProperty("fourthpara5");
		    
        String path = "C:\\Users\\Admin\\Desktop\\PdfFiles\\example"+new Date().getTime()+".pdf";
      //Creating an image Data object
       
      //  String imFile = "C:\\Users\\Admin\\Desktop\\PdfFiles\\graone2.jpg";
     //   String imageFooter = "C:\\Users\\Admin\\Desktop\\PdfFiles\\businesspartnerbb.png";
        
       // String imFile = "D:\\brainquiz-images\\graone2.jpg";
        
        String imFile =this.logoimageLocation + "/file/graone.jpg" ;
        
        String imageFooter = this.logoimageLocation + "/file/businesspartnerbb.png";
        
			/*
			 * PdfWriter writer = new PdfWriter(path); // Creating a PdfDocument object
			 * PdfDocument pdf = new PdfDocument(writer); pdfDoc = pdf; pdfDoc.addNewPage();
			 * Document document = new Document(pdf);
			 */
        
       // PdfDocument pdfDoc = new PdfDocument(new PdfWriter(path));
        
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outputStream));
        
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(36, 36, 120, 36);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);  
        Table table = new Table(1).useAllAvailableWidth();

        float[] columnWidths3 = {500};
  		
  		Table lastBorderLineTable = new Table(UnitValue.createPercentArray(columnWidths3)).useAllAvailableWidth();
        
  		table.addCell(new Cell().add(new Paragraph())
        				.setBold()
        				.setBorder(Border.NO_BORDER)
        				.setBackgroundColor(new DeviceRgb(112,112,112)));
  		
        
        Cell cell = new Cell().add(new Paragraph(footer))
        		.setBorder(Border.NO_BORDER)
        		.setFontSize(8)
        		.setFont(font)
        		.setTextAlignment(TextAlignment.JUSTIFIED);
     //   cell.setBackgroundColor(ColorConstants.ORANGE);
        table.addCell(cell);

        Cell cellfooterImage = new Cell();
        
        ImageData data1 = ImageDataFactory.create(imageFooter);
        
        //Creating an image object
        image = new Image(data1); 

		cellfooterImage.setBorder(Border.NO_BORDER);
		cellfooterImage.add(image);
	
		image.scaleAbsolute(400f, 50f);
		image.setHorizontalAlignment(HorizontalAlignment.CENTER);
	//	footerimageTable.addCell(cellfooterImage);
	//	document.add(footerimageTable);
        
        
       // cell = new Cell().add(new Paragraph(imageFooter));
      //  cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        table.addCell(cellfooterImage);
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new TableFooterEventHandler(table));
        
        //Table for image 
     //   float col = 280f;
        float[] columnWidths11 = {115,385};
     
        Table mainTable = new Table(UnitValue.createPercentArray(columnWidths11)).useAllAvailableWidth();
        
  /* ImageData data = ImageDataFactory.create(imFile);
        Image image = new Image(data); 
        image.scaleAbsolute(100f, 100f);
        image.setBorder(Border.NO_BORDER);  
        
      mainTable.addCell((new Cell().add(image)))
			.setBorder(Border.NO_BORDER);		*/


     Cell cellImage = new Cell();
    	cellImage.setTextAlignment(TextAlignment.CENTER);
        ImageData data = ImageDataFactory.create(imFile);	
        
        //Creating an image object
       Image image = new Image(data); 
        
        image.scaleAbsolute(100f, 100f);
		image.setTextAlignment(TextAlignment.CENTER);
		
		cellImage.setBorder(Border.NO_BORDER);
		cellImage.add(image);
	
		
		mainTable.addCell(cellImage);  
		
		//mainTable.addCell(image);		
		
	/*mainTable.addCell(new Cell().add(new Paragraph())
	        		.setBold()
	        		.setBorder(Border.NO_BORDER));  */
 
		mainTable.addCell(new Cell().add(new Paragraph("\n"+ testName ))
    			.setBold()
    			.setBorder(Border.NO_BORDER)
    			.setFont(font)
    			.setTextAlignment(TextAlignment.CENTER))
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBackgroundColor(new DeviceRgb(112,112,112))
				.setFontSize(24);
		
	/*	mainTable.addCell(new Cell().add(new Paragraph(testName))
        		.setBold()
        		.setBorder(Border.NO_BORDER));  */


      //  mainTable .useAllAvailableWidth(); 
        
        //adding border line to document and image to document
        document.add(mainTable ); 
     
        
        float[] columnWidths = {115, 385};
        Table nameTable = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();
        
        nameTable.addCell(new Cell().add(new Paragraph())
        		.setBold()
        		.setBorder(Border.NO_BORDER));
        
        // Cell for Paragraph
     		Cell userCell = new Cell();
     		userCell.setBold();
     		userCell.setBorder(Border.NO_BORDER);
     		
     		Paragraph RegisterUserParagraph = new Paragraph(stuName).setFont(font);
     					//RegisterUserParagraph.setBold();
     					RegisterUserParagraph.setTextAlignment(TextAlignment.LEFT);
     					RegisterUserParagraph.setFontSize(15);
     				 
     					userCell.add(RegisterUserParagraph);
     					nameTable.addCell(userCell);
     					document.add(nameTable);
     					
        Table dateTable = new Table(UnitValue.createPercentArray(9)).useAllAvailableWidth();
        
        dateTable.addCell(new Cell().add(new Paragraph())
        		.setBold()
        		.setBorder(Border.NO_BORDER)
                .setBackgroundColor(new DeviceRgb(112,112,112)));
        
        dateTable.addCell(new Cell().add(new Paragraph())
        		.setBold()
        		.setBorder(Border.NO_BORDER));
               	
        dateTable.addCell(new Cell().add(new Paragraph())
        		.setBold()
        		.setBorder(Border.NO_BORDER)
        		.setBackgroundColor(new DeviceRgb(112,112,112)));
        
        dateTable.addCell(new Cell().add(new Paragraph())
        		.setBold()
        		.setBorder(Border.NO_BORDER)
        		.setBackgroundColor(new DeviceRgb(112,112,112)));
        
       dateTable.addCell(new Cell().add(new Paragraph())
        		.setBold()
        		.setBorder(Border.NO_BORDER)
        		.setBackgroundColor(new DeviceRgb(112,112,112)));
        
        dateTable.addCell(new Cell().add(new Paragraph())
        		.setBold()
        		.setBorder(Border.NO_BORDER)
        		.setBackgroundColor(new DeviceRgb(112,112,112)));
        
        dateTable.addCell(new Cell().add(new Paragraph())
        		.setBold()
        		.setBorder(Border.NO_BORDER)
        		.setBackgroundColor(new DeviceRgb(112,112,112)));
        
        dateTable.addCell(new Cell().add(new Paragraph())
        		.setBold()
        		.setBorder(Border.NO_BORDER)
        		.setBackgroundColor(new DeviceRgb(112,112,112)));
        
        dateTable.addCell(new Cell().add(new Paragraph())
        		.setBold()
        		.setBorder(Border.NO_BORDER)
        		.setBackgroundColor(new DeviceRgb(112,112,112)));
       
        
        //adding border line to document and image to document
        document.add( dateTable );
        
        //create table for contact

   //    float[] columnWidths = {115, 385};
        
        Table contactTable = new Table(UnitValue.createPercentArray(columnWidths));
        
        		contactTable.setFontSize(10);
        		contactTable.setWidth(500);
        		contactTable.setBorder(Border.NO_BORDER);
        		
           contactTable.addCell(new Cell().add(new Paragraph("Contact"))
        		      .setFontSize(15)
        		      .setFont(font)
        		      .setTextAlignment(TextAlignment.LEFT)
        		   
        			  .setBorder(Border.NO_BORDER));
           
         //  System.out.println(stuName+"hello"+stuName);
    
        contactTable.addCell(new Cell().add(new Paragraph("Dear User "+stuName))
  		      		.setFontSize(10)
  		      		.setFont(font)
  		      		.setBorder(Border.NO_BORDER)
  		      		.setTextAlignment(TextAlignment.LEFT));
       
        //adding contactTable to document
        document.add(contactTable);
        
       Table borderTable = new Table(UnitValue.createPercentArray(7)).useAllAvailableWidth();
        
        borderTable.addCell(new Cell().add(new Paragraph())
        			.setBold()
        			.setBorder(Border.NO_BORDER)
        			.setBackgroundColor(new DeviceRgb(112,112,112)));
        
        borderTable.addCell(new Cell().add(new Paragraph())
        			.setBold()
        			.setBorder(Border.NO_BORDER));
        
        borderTable.addCell(new Cell().add(new Paragraph())
    			.setBold()
    			.setBorder(Border.NO_BORDER));
        
        borderTable.addCell(new Cell().add(new Paragraph())
    			.setBold()
    			.setBorder(Border.NO_BORDER));
        
        borderTable.addCell(new Cell().add(new Paragraph())
    			.setBold()
    			.setBorder(Border.NO_BORDER));
        
        borderTable.addCell(new Cell().add(new Paragraph())
    			.setBold()
    			.setBorder(Border.NO_BORDER));
        
        borderTable.addCell(new Cell().add(new Paragraph())
    			.setBold()
    			.setBorder(Border.NO_BORDER));
        
        document.add( borderTable );
        
        float[] columnWidth = {110, 390};
        Table graoneTable = new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth();
        
        		graoneTable.addCell(new Cell().add(new Paragraph("info@graone.co.in"))
        					.setFontSize(11)
        					.setFont(font)
        					.setTextAlignment(TextAlignment.LEFT)
        					.setBorder(Border.NO_BORDER));
        		
        		graoneTable.addCell(new Cell().add(new Paragraph(firstpara1))
      		      		.setFontSize(10)
      		      		.setFont(font)
      		      		.setBorder(Border.NO_BORDER)
      		      		.setTextAlignment(TextAlignment.LEFT));
        		
        		graoneTable.addCell(new Cell().add(new Paragraph())
        				.setBold()
        				.setBorder(Border.NO_BORDER));
        		
        		graoneTable.addCell(new Cell().add(new Paragraph("Assessment ID: "  +stuName +" being assessed for "+ testCode +" on "+ subDate))
      		      		.setFontSize(10)
      		      		.setFont(font)
      		      		.setBorder(Border.NO_BORDER)
      		      		.setTextAlignment(TextAlignment.LEFT));
        		document.add(graoneTable);
        		
        		//added new paragraph
                document.add(new Paragraph());
                
        Table mainInfoTable = new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth();	
        
        		mainInfoTable.addCell(new Cell().add(new Paragraph())
            				.setBold()
            				.setBorder(Border.NO_BORDER));
        					
        		mainInfoTable.addCell(new Cell().add(new Paragraph(firstpara))
      		      			.setFontSize(10)
      		      			.setFont(font)
      		      			.setBorder(Border.NO_BORDER)
      		      			.setTextAlignment(TextAlignment.LEFT));
        		
        		document.add(mainInfoTable);
        		
        		 
        		 Table info2Table = new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth();
        		 
        		 info2Table.addCell(new Cell().add(new Paragraph())
         					.setBold()
         					.setBorder(Border.NO_BORDER));
        		 
        		 info2Table.addCell(new Cell().add(new Paragraph(secondpara + result.size()+" " + secondpara1))
       		      			.setFontSize(10)
       		      			.setFont(font)
       		      			.setBorder(Border.NO_BORDER)
       		      			.setTextAlignment(TextAlignment.LEFT));
        		 
        		 document.add(info2Table);
        		 
        		 document.add(new Paragraph());
        		 
        		 

        		 
        		 float[] columnWidths1 = {110, 30, 200,20,20,20,20,20,20,20,20};
        		 
         Table tab1Table = new Table(UnitValue.createPercentArray(columnWidths1)).useAllAvailableWidth();
         
         ArrayList<String> value2 = new ArrayList<String>();
         
      //   String count ="1";
         
        int count=1;
         
        // int i = Integer.parseInt(count); 
         
         for(ResultPdfResponse resp: result) {
	        //	PdfNull cell = new PdfPCell(new Paragraph(resp.getCategoryName()));
	        //	PdfNull color = new PdfPCell(new Paragraph(""));
	        //	detailsTable.addCell(cell);
        	 
        	 
        	 System.out.println("color"+resp.getColors());

        	 value2.add(resp.getColors());

	        	
				/*
				 * Cell color; 
				 * if(resp.getColors().equals("red")) color.setBackgroundColor(new
				 * DeviceRgb(255, 0, 0)); else if(resp.getColors().equals("green"))
				 * color.setBackgroundColor(new DeviceRgb(60, 179, 113)); else
				 * color.setBackgroundColor(new DeviceRgb(255, 204, 0));
				 * 
				 * tab1Table.addCell(color);
				 */
        	 
    		 tab1Table.addCell(new Cell().add(new Paragraph())
  					.setBold()
  					.setBorder(Border.NO_BORDER));
    		 
    		 tab1Table.addCell(new Cell().add(new Paragraph( Integer. toString(count)))
   		      			.setFontSize(10)
   		      			.setFont(font)
   		      			.setBorder(Border.NO_BORDER)
   		      			.setTextAlignment(TextAlignment.LEFT));
    		 
    		
			tab1Table.addCell(new Cell().add(new Paragraph(resp.getCategoryName()))
     		      	.setFontSize(10)
     		      	.setFont(font)
    		      	.setBorder(Border.NO_BORDER)
    		      	.setTextAlignment(TextAlignment.LEFT));
    		 
    		 tab1Table.addCell(new Cell().add(new Paragraph())
   					.setBold()
   					.setBorder(Border.NO_BORDER));
    		 
    		 tab1Table.addCell(new Cell().add(new Paragraph())
   					.setBold()
   					.setBorder(Border.NO_BORDER));
    		 
    		 
    		
    		 if(resp.getColors().equals("red"))
    		 {
    		 tab1Table.addCell(new Cell().add(new Paragraph())
  		      		.setFontSize(10)
  		      		.setFont(font)
  		      		.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
 		      		.setBackgroundColor(new DeviceRgb(255, 0, 0))
 		      		.setTextAlignment(TextAlignment.CENTER));
    		 }else if(resp.getColors().equals("green"))
    		 {
    		 tab1Table.addCell(new Cell().add(new Paragraph())
  		      		.setFontSize(10)
  		      		.setFont(font)
  		      		.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
 		      		.setBackgroundColor(new DeviceRgb(60, 179, 113))
 		      		.setTextAlignment(TextAlignment.CENTER));
    		 }
    		 else if(resp.getColors().equals("amber"))
    		 {
    		 tab1Table.addCell(new Cell().add(new Paragraph())
  		      		.setFontSize(10)
  		      		.setFont(font)
  		      		.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
 		      		.setBackgroundColor(new DeviceRgb(255, 204, 0))
 		      		.setTextAlignment(TextAlignment.CENTER));
    		 }
    		 
    		 	
    		 tab1Table.addCell(new Cell().add(new Paragraph())
   						.setBold()
   						.setBorder(Border.NO_BORDER));
    		 
    		 tab1Table.addCell(new Cell().add(new Paragraph())
    					.setBold()
    					.setBorder(Border.NO_BORDER));
    		 
    		 tab1Table.addCell(new Cell().add(new Paragraph())
    					.setBold()
    					.setBorder(Border.NO_BORDER));
    		 
    		 tab1Table.addCell(new Cell().add(new Paragraph())
    					.setBold()
    					.setBorder(Border.NO_BORDER));
    		 
    		 tab1Table.addCell(new Cell().add(new Paragraph())
 					.setBold()
 					.setBorder(Border.NO_BORDER));
    		 
    		 count++;
	        
	        }
        		 
          document.add(tab1Table);
        		 
        		
        /*	Table tab2Table = new Table(UnitValue.createPercentArray(columnWidths1)).useAllAvailableWidth();
        		 
        		 tab2Table.addCell(new Cell().add(new Paragraph())
       					.setBold()
       					.setBorder(Border.NO_BORDER));
        		 
        		 tab2Table.addCell(new Cell().add(new Paragraph("2"))
        		      	.setFontSize(10)
       		      		.setBorder(Border.NO_BORDER)
       		      		.setTextAlignment(TextAlignment.LEFT));
         		 
         		 tab2Table.addCell(new Cell().add(new Paragraph("param 2"))
         		      		.setFontSize(10)
        		      		.setBorder(Border.NO_BORDER)
        		      		.setTextAlignment(TextAlignment.LEFT));
         		 
         		 tab2Table.addCell(new Cell().add(new Paragraph())
      		      		.setFontSize(10)
     		      		.setBorder(Border.NO_BORDER));
     		      		
         		tab2Table.addCell(new Cell().add(new Paragraph())
     					.setBold()
     					.setBorder(Border.NO_BORDER));
         		
         		tab2Table.addCell(new Cell().add(new Paragraph())
      		      		.setFontSize(10)
      		      		.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
     		      		.setBackgroundColor(new DeviceRgb(60, 179, 113)));
         		
         		tab2Table.addCell(new Cell().add(new Paragraph())
     					.setBold()
     					.setBorder(Border.NO_BORDER));
         		tab2Table.addCell(new Cell().add(new Paragraph())
     					.setBold()
     					.setBorder(Border.NO_BORDER));
         		tab2Table.addCell(new Cell().add(new Paragraph())
     					.setBold()
     					.setBorder(Border.NO_BORDER));
         		tab2Table.addCell(new Cell().add(new Paragraph())
     					.setBold()
     					.setBorder(Border.NO_BORDER));
         		tab2Table.addCell(new Cell().add(new Paragraph())
     					.setBold()
     					.setBorder(Border.NO_BORDER));
         	
         		 document.add(tab2Table);
         		 
         	Table tab3Table = new Table(UnitValue.createPercentArray(columnWidths1)).useAllAvailableWidth();
         	 	
         	 	tab3Table.addCell(new Cell().add(new Paragraph())
         	 			.setBold()
         	 			.setBorder(Border.NO_BORDER));
        		 
        		 tab3Table.addCell(new Cell().add(new Paragraph("3"))
        		      		.setFontSize(10)
        		      		.setBorder(Border.NO_BORDER)
        		      		.setTextAlignment(TextAlignment.LEFT));
         		 
         		 tab3Table.addCell(new Cell().add(new Paragraph("Param 3"))
         		      		.setFontSize(10)
        		      		.setBorder(Border.NO_BORDER)
        		      		.setTextAlignment(TextAlignment.LEFT));
         		 
         		 tab3Table.addCell(new Cell().add(new Paragraph(""))
     		      		.setBorder(Border.NO_BORDER)
     		      		.setTextAlignment(TextAlignment.CENTER));
         		 
         		 tab3Table.addCell(new Cell().add(new Paragraph(""))
      		      		.setBorder(Border.NO_BORDER)
      		      		.setTextAlignment(TextAlignment.CENTER));
         		 
         		tab3Table.addCell(new Cell().add(new Paragraph())
         					.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
         					.setBackgroundColor(new DeviceRgb(60, 179, 113))
         					.setTextAlignment(TextAlignment.CENTER));
         		
         		tab3Table.addCell(new Cell().add(new Paragraph())
     					.setBold()
     					.setBorder(Border.NO_BORDER));
         		
         		tab3Table.addCell(new Cell().add(new Paragraph())
     					.setBold()
     					.setBorder(Border.NO_BORDER));
         		
         		tab3Table.addCell(new Cell().add(new Paragraph())
     					.setBold()
     					.setBorder(Border.NO_BORDER));
         		
         		tab3Table.addCell(new Cell().add(new Paragraph())
     					.setBold()
     					.setBorder(Border.NO_BORDER));
         		
         		tab3Table.addCell(new Cell().add(new Paragraph())
     					.setBold()
     					.setBorder(Border.NO_BORDER));
         		
         		 document.add(tab3Table);
         		 
         		Table tab4Table = new Table(UnitValue.createPercentArray(columnWidths1)).useAllAvailableWidth();
         			
         			tab4Table.addCell(new Cell().add(new Paragraph())
         	 			.setBold()
         	 			.setBorder(Border.NO_BORDER));
       		 
         			tab4Table.addCell(new Cell().add(new Paragraph("4"))
       		      		.setFontSize(10)
      		      		.setBorder(Border.NO_BORDER)
      		      		.setTextAlignment(TextAlignment.LEFT));
        		 
         			tab4Table.addCell(new Cell().add(new Paragraph("Param 4"))
        		      		.setFontSize(10)
        		      		.setBorder(Border.NO_BORDER)
        		      		.setTextAlignment(TextAlignment.LEFT));
         			
         			tab4Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
        		 
         			tab4Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
         			
         			tab4Table.addCell(new Cell().add(new Paragraph())
         					.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
         					.setBackgroundColor(new DeviceRgb(60, 179, 113))
         					.setTextAlignment(TextAlignment.CENTER));
         			
         			tab4Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
         			
         			tab4Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
         			
         			tab4Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
         			
         			tab4Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
         			
         			tab4Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
         	
         			document.add(tab4Table);  */
         		 
         			
         			Table tab5Table = new Table(UnitValue.createPercentArray(columnWidths1)).useAllAvailableWidth();
         			
         			
         			tab5Table.addCell(new Cell().add(new Paragraph())
             	 			.setBold()
             	 			.setBorder(Border.NO_BORDER));
              		 
         			tab5Table.addCell(new Cell().add(new Paragraph( Integer. toString(count)))
       		      		.setFontSize(10)
       		      		.setFont(font)
      		      		.setBorder(Border.NO_BORDER)
      		      		.setTextAlignment(TextAlignment.LEFT));
        		 
         			tab5Table.addCell(new Cell().add(new Paragraph("Overall assessment score / indicator"))
        		      	.setFontSize(10)
        		      	.setFont(font)
       		      		.setBorder(Border.NO_BORDER)
       		      		.setTextAlignment(TextAlignment.LEFT));
         			
         			tab5Table.addCell(new Cell().add(new Paragraph(""))
    		      		.setBorder(Border.NO_BORDER)
    		      		.setTextAlignment(TextAlignment.CENTER));
         			
         			tab5Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
         			
         			
         			if(value2.contains("red")) {
         				
         				tab5Table.addCell(new Cell().add(new Paragraph())
             					.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
             					.setBackgroundColor(new DeviceRgb(255, 0, 0))
             					.setTextAlignment(TextAlignment.CENTER));
         			}
         			else if(value2.contains("Amber")) {
         	  	        	tab5Table.addCell(new Cell().add(new Paragraph())
                 					.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
                 					.setBackgroundColor(new DeviceRgb(60, 179, 113))
                 					.setTextAlignment(TextAlignment.CENTER));
         	  	        }       	  	         	
         	  	        else {
         	  	        	tab5Table.addCell(new Cell().add(new Paragraph())
                 					.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
                 					.setBackgroundColor(new DeviceRgb(255, 204, 0))
                 					.setTextAlignment(TextAlignment.CENTER));
         	  	        }
         		/*	tab5Table.addCell(new Cell().add(new Paragraph())
         					.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
         					.setBackgroundColor(new DeviceRgb(255, 0, 0))
         					.setTextAlignment(TextAlignment.CENTER)); */
         			
         			tab5Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
         			
         			tab5Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
         			
         			tab5Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
         			
         			tab5Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
         			
         			tab5Table.addCell(new Cell().add(new Paragraph(" "))
         					.setBorder(Border.NO_BORDER)
         					.setTextAlignment(TextAlignment.CENTER));
       
         			document.add(tab5Table); 
         			
         			document.add(new Paragraph());
         			
         			
           		 Table info3Table = new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth();
           		 					
           		info3Table.addCell(new Cell().add(new Paragraph())
           				.setBold()
           				.setBorder(Border.NO_BORDER));
           		 
           		info3Table.addCell(new Cell().add(new Paragraph(interpretation))
       		      		.setFontSize(10)
       		      		.setFont(font)
       		      		.setBorder(Border.NO_BORDER)
       		      		.setTextAlignment(TextAlignment.LEFT));
         			
         		document.add(info3Table);
         		
         		document.add(new Paragraph());
         		
         		 float[] columnWidths2 = {110, 20, 45, 325};
         		 
         		Table tab6Table = new Table(UnitValue.createPercentArray(columnWidths2)).useAllAvailableWidth();
         		
         		tab6Table.addCell(new Cell().add(new Paragraph())
         	 			.setBold()
         	 			.setBorder(Border.NO_BORDER));
          		 
     			tab6Table.addCell(new Cell().add(new Paragraph(""))
     					.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
     		      		.setBackgroundColor(new DeviceRgb(60, 179, 113))
     		      		.setFont(font)
  		      			.setTextAlignment(TextAlignment.LEFT));
    		 
     			tab6Table.addCell(new Cell().add(new Paragraph(keycolor1))
    		      	.setFontSize(9)
    		      	.setFont(font)
   		      		.setBorder(Border.NO_BORDER)
   		      		.setTextAlignment(TextAlignment.LEFT));
     			
     			tab6Table.addCell(new Cell().add(new Paragraph(keycolor11))
 		      		.setFontSize(9)
 		      		.setFont(font)
		      		.setBorder(Border.NO_BORDER)
		      		.setBold()
		      		.setTextAlignment(TextAlignment.LEFT));
     			
     			document.add(tab6Table);
     			
     			Table tab7Table = new Table(UnitValue.createPercentArray(columnWidths2)).useAllAvailableWidth();
         			
     			tab7Table.addCell(new Cell().add(new Paragraph())
         	 			.setBold()
         	 			.setBorder(Border.NO_BORDER));
          		 
     			tab7Table.addCell(new Cell().add(new Paragraph(""))
     					.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
     		      		.setBackgroundColor(new DeviceRgb(255, 204, 0))
     		      		.setTextAlignment(TextAlignment.LEFT));
    		 
     			tab7Table.addCell(new Cell().add(new Paragraph(keycolor2))
    		      	.setFontSize(9)
    		      	.setFont(font)
   		      		.setBorder(Border.NO_BORDER)
   		      		.setTextAlignment(TextAlignment.LEFT));
     			
     			tab7Table.addCell(new Cell().add(new Paragraph(keycolor22))
 		      		.setFontSize(9)
 		      		.setFont(font)
		      		.setBorder(Border.NO_BORDER)
		      		.setBold()
		      		.setTextAlignment(TextAlignment.LEFT));
     			
     			document.add(tab7Table);
     			
     			Table tab8Table = new Table(UnitValue.createPercentArray(columnWidths2)).useAllAvailableWidth();
         			
     			tab8Table.addCell(new Cell().add(new Paragraph())
         	 			.setBold()
         	 			.setBorder(Border.NO_BORDER));
          		 
     			tab8Table.addCell(new Cell().add(new Paragraph(""))
     					.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255,255,255)), 3))
     		      		.setBackgroundColor(new DeviceRgb(255, 0, 0))
     		      		.setTextAlignment(TextAlignment.LEFT));
    		 
     			tab8Table.addCell(new Cell().add(new Paragraph(keycolor3))
    		      	.setFontSize(9)
    		      	.setFont(font)
   		      		.setBorder(Border.NO_BORDER)
   		      		.setTextAlignment(TextAlignment.LEFT));
     			
     			tab8Table.addCell(new Cell().add(new Paragraph(keycolor33))
 		      		.setFontSize(9)
 		      		.setFont(font)
		      		.setBorder(Border.NO_BORDER)
		      		.setBold()
		      		.setTextAlignment(TextAlignment.LEFT));
     			
     			document.add(tab8Table);
         		
     			document.add(new Paragraph());
     			 
     			 
       		 Table info4Table = new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth();
       		 
       		 info4Table.addCell(new Cell().add(new Paragraph())
        					.setBold()
        					.setBorder(Border.NO_BORDER));
       		 
       		 info4Table.addCell(new Cell().add(new Paragraph(thirdpara +result.size()+ " " + thirdpara1))
      		      			.setFontSize(10)
      		      			.setFont(font)
      		      			.setBorder(Border.NO_BORDER)
      		      			.setTextAlignment(TextAlignment.LEFT));
       		 
       		 document.add(info4Table);
         		
       		 
       		document.add(new Paragraph());
			 
			 
      		 Table info5Table = new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth();
      		 
      		 info5Table.addCell(new Cell().add(new Paragraph())
       					.setBold()
       					.setBorder(Border.NO_BORDER));
      		 
      		 info5Table.addCell(new Cell().add(new Paragraph(disclaimer))
     		      			.setFontSize(10)
     		      			.setFont(font)
     		      			.setBorder(Border.NO_BORDER)
     		      			.setTextAlignment(TextAlignment.LEFT));
      		 
      		
         	
      		//document.add(new Paragraph());
      		
			/*
			 * float[] columnWidths3 = {500};
			 * 
			 * Table lastBorderLineTable = new
			 * Table(UnitValue.createPercentArray(columnWidths3)).useAllAvailableWidth();
			 * 
			 * lastBorderLineTable.addCell(new Cell().add(new Paragraph()) .setBold()
			 * .setBorder(Border.NO_BORDER) .setBackgroundColor(new
			 * DeviceRgb(112,112,112)));
			 * 
			 * document.add(lastBorderLineTable);
			 */
			/*
			 * Table lastTextTable = new
			 * Table(UnitValue.createPercentArray(columnWidths3)).useAllAvailableWidth();
			 * 
			 * lastTextTable.addCell(new Cell().add(new Paragraph(footer))
			 * .setBorder(Border.NO_BORDER) .setFontSize(10)
			 * .setTextAlignment(TextAlignment.JUSTIFIED));
			 * 
			 * document.add(lastTextTable);
			 */
      		//Adding footer image
         
     //     Table footerimageTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
          
			/*
			 * Cell cellfooterImage = new Cell();
			 * 
			 * ImageData data1 = ImageDataFactory.create(imageFooter);
			 * 
			 * //Creating an image object image = new Image(data1);
			 * 
			 * cellfooterImage.setBorder(Border.NO_BORDER); cellfooterImage.add(image);
			 * image.scaleAbsolute(550f, 75f); footerimageTable.addCell(cellfooterImage);
			 * document.add(footerimageTable);
			 */
      		// Adding new page
      		// pdfDoc.addNewPage();
      		info5Table.addCell(new Cell().add(new Paragraph())
   					.setBold()
   					.setBorder(Border.NO_BORDER));
      		 info5Table.addCell(new Cell().add(new Paragraph(fourthpara))
		      			.setFontSize(10)
		      			.setFont(font)
		      			.setBorder(Border.NO_BORDER)
		      			.setTextAlignment(TextAlignment.LEFT));
      		 
      		info5Table.addCell(new Cell().add(new Paragraph())
   					.setBold()
   					.setBorder(Border.NO_BORDER));
      		 info5Table.addCell(new Cell().add(new Paragraph(fourthpara1))
		      			.setFontSize(10)
		      			.setFont(font)
		      			.setBorder(Border.NO_BORDER)
		      			.setTextAlignment(TextAlignment.LEFT));
      		 
      		info5Table.addCell(new Cell().add(new Paragraph())
   					.setBold()
   					.setBorder(Border.NO_BORDER));
      		 info5Table.addCell(new Cell().add(new Paragraph(fourthpara2))
		      			.setFontSize(10)
		      			.setFont(font)
		      			.setBorder(Border.NO_BORDER)
		      			.setTextAlignment(TextAlignment.LEFT));
      		 
      		info5Table.addCell(new Cell().add(new Paragraph())
   					.setBold()
   					.setBorder(Border.NO_BORDER));
      		 info5Table.addCell(new Cell().add(new Paragraph(fourthpara3))
		      			.setFontSize(10)
		      			.setFont(font)
		      			.setBorder(Border.NO_BORDER)
		      			.setTextAlignment(TextAlignment.LEFT));
      		 
      		info5Table.addCell(new Cell().add(new Paragraph())
   					.setBold()
   					.setBorder(Border.NO_BORDER));
      		 info5Table.addCell(new Cell().add(new Paragraph(fourthpara4))
		      			.setFontSize(10)
		      			.setFont(font)
		      			.setBorder(Border.NO_BORDER)
		      			.setTextAlignment(TextAlignment.LEFT));
      		 
      		info5Table.addCell(new Cell().add(new Paragraph())
   					.setBold()
   					.setBorder(Border.NO_BORDER));
      		 info5Table.addCell(new Cell().add(new Paragraph(fourthpara5))
		      			.setFontSize(10)
		      			.setFont(font)
		      			.setBorder(Border.NO_BORDER)
		      			.setTextAlignment(TextAlignment.LEFT));
      		
      		 document.add(info5Table);
      		
      		document.add(new Paragraph());
      		document.add(new Paragraph());
      		
      	/*	float[] columnWidths4 = {55, 445};
      		 
      		Table firstTable = new Table(UnitValue.createPercentArray(columnWidths4)).useAllAvailableWidth();
      		
      				firstTable.addCell(new Cell().add(new Paragraph())
      						.setBorder(Border.NO_BORDER));
      		
      				firstTable.addCell(new Cell().add(new Paragraph("Legend :"))
      						.setFontSize(11)
      						.setTextAlignment(TextAlignment.LEFT)
      						.setBorder(Border.NO_BORDER));
      				
      				firstTable.addCell(new Cell().add(new Paragraph())
      						.setBorder(Border.NO_BORDER));
      				
      				firstTable.addCell(new Cell().add(new Paragraph("1."))
          					.setFontSize(11)
          					.setTextAlignment(TextAlignment.LEFT)
             		      	.setBorder(Border.NO_BORDER));
      				
      				document.add(firstTable);
      				
      			
      				float[] columnWidths5 = {55,230,20,195};
      				
      				Table secondTable = new Table(UnitValue.createPercentArray(columnWidths5)).useAllAvailableWidth();
      				
      						secondTable.addCell(new Cell().add(new Paragraph())
      									.setBorder(Border.NO_BORDER));
      						
      						secondTable.addCell(new Cell().add(new Paragraph("Purple rectanle border with light purple highlight"))
      	          						.setFontSize(11)
      	          						.setTextAlignment(TextAlignment.LEFT)
      	          						.setBorder(Border.NO_BORDER));	
      						
      					secondTable.addCell(new Cell().add(new Paragraph(""))
      		     					.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(115, 0, 153)), 3))
      		     		      		.setBackgroundColor(new DeviceRgb(242, 204, 255))
      		     		      		.setTextAlignment(TextAlignment.LEFT));
      				
      					secondTable.addCell(new Cell().add(new Paragraph())
									.setBorder(Border.NO_BORDER));
      				document.add(secondTable);
      				
      				Table thirdTable = new Table(UnitValue.createPercentArray(columnWidths4)).useAllAvailableWidth();
      				
      						thirdTable.addCell(new Cell().add(new Paragraph())
          							.setBorder(Border.NO_BORDER));
      						
      						thirdTable.addCell(new Cell().add(new Paragraph(keyinfo1))
      	      						.setFontSize(11)
      	      						.setTextAlignment(TextAlignment.LEFT)
      	      						.setBorder(Border.NO_BORDER));
      						
      						document.add(thirdTable);
      						
      				Table fourthTable = new Table(UnitValue.createPercentArray(columnWidths4)).useAllAvailableWidth();
      						
      				fourthTable.addCell(new Cell().add(new Paragraph())
      							.setBorder(Border.NO_BORDER));
      				
      				fourthTable.addCell(new Cell().add(new Paragraph("2."))
      						.setFontSize(11)
          					.setTextAlignment(TextAlignment.LEFT)
             		      	.setBorder(Border.NO_BORDER));
      				
      						document.add(fourthTable);
      						
      				float[] columnWidths6 = {55,20,40,385};
      				
      				Table fourth1Table = new Table(UnitValue.createPercentArray(columnWidths6)).useAllAvailableWidth();
      				
      				fourth1Table.addCell(new Cell().add(new Paragraph())
  								.setBorder(Border.NO_BORDER));
      				
      				fourth1Table.addCell(new Cell().add(new Paragraph("Blue"))
          					.setFontSize(11)
          					.setTextAlignment(TextAlignment.LEFT)
             		      	.setBorder(Border.NO_BORDER));
      				
      				
      				fourth1Table.addCell(new Cell().add(new Paragraph("highlight"))
      							.setBorder(Border.NO_BORDER)
		     		      		.setBackgroundColor(new DeviceRgb(77, 255, 255))
		     		      		.setTextAlignment(TextAlignment.LEFT));
      				
      				fourth1Table.addCell(new Cell().add(new Paragraph())
								.setBorder(Border.NO_BORDER));
      				
      				document.add(fourth1Table);
      				
      				Table fifthTable = new Table(UnitValue.createPercentArray(columnWidths4)).useAllAvailableWidth();
      				
      				fifthTable.addCell(new Cell().add(new Paragraph())
  							.setBorder(Border.NO_BORDER));
      				
      				fifthTable.addCell(new Cell().add(new Paragraph(keyinfo2))
	      						.setFontSize(11)
	      						.setTextAlignment(TextAlignment.LEFT)
	      						.setBorder(Border.NO_BORDER));
      				
      				document.add(fifthTable);
      				
      				Table sixthTable = new Table(UnitValue.createPercentArray(columnWidths4)).useAllAvailableWidth();
      							
      						sixthTable.addCell(new Cell().add(new Paragraph())
      	      						.setBorder(Border.NO_BORDER));
      						
      						sixthTable.addCell(new Cell().add(new Paragraph("3."))
      	      						.setFontSize(11)
      	      						.setTextAlignment(TextAlignment.LEFT)
      	      						.setBorder(Border.NO_BORDER));
      						
      				document.add(sixthTable);
      				
      				float[] columnWidths7 = {55,25,40,10,20,350};
      				
      				Table sixth1Table = new Table(UnitValue.createPercentArray(columnWidths7)).useAllAvailableWidth();
      				
      					sixth1Table.addCell(new Cell().add(new Paragraph())
	      							.setBorder(Border.NO_BORDER));
      					
      					sixth1Table.addCell(new Cell().add(new Paragraph("Yellow"))
  	      						.setFontSize(11)
  	      						.setTextAlignment(TextAlignment.LEFT)
  	      						.setBorder(Border.NO_BORDER));
      					
      					sixth1Table.addCell(new Cell().add(new Paragraph("highlight"))
      							.setBorder(Border.NO_BORDER)
		     		      		.setBackgroundColor(new DeviceRgb(255, 255, 26))
		     		      		.setTextAlignment(TextAlignment.LEFT));
      					
      					sixth1Table.addCell(new Cell().add(new Paragraph("or"))
  	      						.setFontSize(11)
  	      						.setTextAlignment(TextAlignment.LEFT)
  	      						.setBorder(Border.NO_BORDER));
      					
      					sixth1Table.addCell(new Cell().add(new Paragraph(""))
  		     					.setBorder(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(255, 214, 51)), 3))
  		     		      		.setBackgroundColor(new DeviceRgb(255, 245, 204))
  		     		      		.setTextAlignment(TextAlignment.LEFT));
      					
      					sixth1Table.addCell(new Cell().add(new Paragraph())
      							.setBorder(Border.NO_BORDER));
      					
      					document.add(sixth1Table);
      					
      					Table sixth2Table = new Table(UnitValue.createPercentArray(columnWidths4)).useAllAvailableWidth();
      					
      							sixth2Table.addCell(new Cell().add(new Paragraph())
      		  							.setBorder(Border.NO_BORDER));
      							
      							sixth2Table.addCell(new Cell().add(new Paragraph(keyinfo3))
      			      						.setFontSize(11)
      			      						.setTextAlignment(TextAlignment.LEFT)
      			      						.setBorder(Border.NO_BORDER));
      							
      							document.add(sixth2Table);		*/
   
        	//closing the document
            document.close();
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	
	}
	
	private static class TableFooterEventHandler implements IEventHandler {
        private Table table;

        public TableFooterEventHandler(Table table) {
            this.table = table;
        }

        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

            System.out.println("======getPageSize====="+page.getPageSize().getWidth());
            
            new Canvas(canvas, new Rectangle(50, 0, page.getPageSize().getWidth() -100, 90))
                    .add(table)
                    .close();
        }

    }
}

