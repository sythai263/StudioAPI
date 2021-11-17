package com.studio.controller.api;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.studio.model.InfoAPI;

@Controller
@RestController
@RequestMapping("api")
public class API {
	private final String APPLICATION_NAME = "Studio";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    private final String CREDENTIALS_FILE_PATH = "/client_secret.json";

	@RequestMapping()
	public ResponseEntity<ArrayList<InfoAPI>> getAPI(){
		ArrayList<InfoAPI> list = new ArrayList<InfoAPI>();
		list.add(new InfoAPI("GET", "api/employees"));
		list.add(new InfoAPI("GET, DELETE", "api/employees/{id}"));
		list.add(new InfoAPI("GET", "api/employees/phone={phone}"));
		list.add(new InfoAPI("POST, PUT", "api/employees/employee"));
		list.add(new InfoAPI("POST", "api/employees/change-avatar/{id}"));
		
		
		list.add(new InfoAPI("GET", "api/accounts"));
		list.add(new InfoAPI("GET, DELETE", "api/accounts/{phone}"));
		list.add(new InfoAPI("POST, PUT", "api/accounts/account"));
		list.add(new InfoAPI("POST", "api/accounts/account/change-password"));

		list.add(new InfoAPI("GET", "api/prices"));
		list.add(new InfoAPI("GET", "api/prices/{id}"));
		list.add(new InfoAPI("GET", "api/prices/service={id}"));
		list.add(new InfoAPI("POST", "api/prices/price"));
		
		list.add(new InfoAPI("GET", "api/detail-order"));
		list.add(new InfoAPI("GET", "api/detail-order/date={date}"));
		list.add(new InfoAPI("GET", "api/detail-order/from-to"));
		list.add(new InfoAPI("GET", "api/detail-order/order={id}"));
		list.add(new InfoAPI("POST, DELETE", "api/detail-order/detail"));
		
		list.add(new InfoAPI("GET", "api/specializes"));
		list.add(new InfoAPI("GET, DELETE", "api/specializes/{id}"));
		list.add(new InfoAPI("POST, PUT", "api/specializes/specialize"));

		list.add(new InfoAPI("GET", "api/rate-employee"));
		list.add(new InfoAPI("GET", "api/rate-employee/employee={id}"));
		list.add(new InfoAPI("GET", "api/rate-employee/customer={id}"));
		list.add(new InfoAPI("POST", "api/rate-employee/rate"));

		list.add(new InfoAPI("GET", "api/rate-service"));
		list.add(new InfoAPI("GET", "api/rate-service/service={id}"));
		list.add(new InfoAPI("GET", "api/rate-employee/customer={id}"));
		list.add(new InfoAPI("POST", "api/rate-service/rate"));

		list.add(new InfoAPI("GET", "api/services"));
		list.add(new InfoAPI("GET, DELETE", "api/services/{id}"));
		list.add(new InfoAPI("POST, PUT", "api/services/service"));

		list.add(new InfoAPI("GET","api/orders"));
		list.add(new InfoAPI("GET, DELETE","api/orders/{id}"));
		list.add(new InfoAPI("GET","api/orders/customer={id}"));
		list.add(new InfoAPI("POST, PUT","api/orders/order"));

		list.add(new InfoAPI("GET", "api/customers"));
		list.add(new InfoAPI("GET, DELETE", "api/customers/{id}"));
		list.add(new InfoAPI("POST, PUT", "api/customers/customer"));
		list.add(new InfoAPI("POST", "api/customer/change-avatar/{id}"));

		list.add(new InfoAPI("GET", "api/salaries"));
		list.add(new InfoAPI("GET", "api/salaries/{id}"));
		list.add(new InfoAPI("GET", "api/salaries/employee={id}"));
		list.add(new InfoAPI("GET", "api/salaries/month={month}&year={year}"));
		list.add(new InfoAPI("GET", "api/salaries/month"));
		list.add(new InfoAPI("GET", "api/salaries/year"));
		list.add(new InfoAPI("GET", "api/salaries/from-to"));
		list.add(new InfoAPI("POST, PUT", "api/salaries/salary"));

		list.add(new InfoAPI("GET","api/rules"));
		list.add(new InfoAPI("GET","api/rules/{id}"));
		list.add(new InfoAPI("POST, PUT","api/rules/rule"));


		list.add(new InfoAPI("GET","api/assignments"));
		list.add(new InfoAPI("GET","api/assignments/{id}"));
		list.add(new InfoAPI("GET","api/assignments/now"));
		list.add(new InfoAPI("GET","api/assignments/employee={id}"));
		list.add(new InfoAPI("GET","api/assignments/employee={id}&now"));
		list.add(new InfoAPI("GET","api/assignments/order={id}"));
		list.add(new InfoAPI("GET","api/assignments/order={id}&now"));
		list.add(new InfoAPI("POST, PUT","api/assignments/assignment"));
		list.add(new InfoAPI("GET","api/assignments/hoa-hong/employee={id}&month={month}"));
		list.add(new InfoAPI("GET","api/assignmentshoa-hong/employee={id}&year={year}"));
		

		list.add(new InfoAPI("GET","api/products"));
		list.add(new InfoAPI("GET","api/products/{id}"));
		list.add(new InfoAPI("GET","api/products/service={id}"));
		list.add(new InfoAPI("POST, PUT","api/products/product"));

		list.add(new InfoAPI("GET","api/fouls"));
		list.add(new InfoAPI("GET","api/fouls/{id}"));
		list.add(new InfoAPI("GET","api/fouls/employee={id}"));
		list.add(new InfoAPI("GET","api/fouls/rule={id}"));
		list.add(new InfoAPI("GET","api/fouls/month={month}&year={year}"));
		list.add(new InfoAPI("GET","api/fouls/employee={id}&now"));
		list.add(new InfoAPI("GET","api/fouls/foul-now"));
		list.add(new InfoAPI("GET","api/fouls/from-to"));
		list.add(new InfoAPI("GET","api/fouls/employee={id}&from-to"));
		list.add(new InfoAPI("POST","api/fouls/foul"));


		return new ResponseEntity<ArrayList<InfoAPI>>(list, HttpStatus.OK);
	}
	
	@Autowired
    private GoogleCredential googleCredential;

    @Bean
    public Drive getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT,
                JacksonFactory.getDefaultInstance(), googleCredential)
                .build();
    }

    @Bean
    public GoogleCredential googleCredential() throws GeneralSecurityException, IOException {
        Collection<String> elenco = new ArrayList<String>();
        elenco.add("https://www.googleapis.com/auth/drive");
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        return new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId("admin-536@studio-ptithcm.iam.gserviceaccount.com")
                .setServiceAccountScopes(elenco)
                .setServiceAccountPrivateKeyFromP12File(new java.io.File("/home/sythai263/Documents/studio-ptithcm.p12"))
                .build();
    }
    @Autowired
    private Drive googleDrive;
    
    @RequestMapping( value = "upload/avatar", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadAvatar(@RequestParam("file") MultipartFile files) throws IOException{
    	
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, 1);
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
    	java.io.File cvf = convert(files);
        
    	File fileMetadata = new File();
    	fileMetadata.setParents(Collections.singletonList("14M1Mec75EE5yE5FCtNW52eQ4ybrrVg65"))
    		.setName("avatar_"+format.format(cal.getTime())+".jpg");
    	FileContent mediaContent = new FileContent("image/jpeg", cvf);
    	File file = googleDrive.files().create(fileMetadata, mediaContent)
    	    .setFields("id")
    	    .execute();
    	return new ResponseEntity<Object>("https://drive.google.com/uc?export=view&id="+file.getId(), HttpStatus.OK);
    }
    @RequestMapping( value = "upload/check-in", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadCheckIn(@RequestParam("file") MultipartFile files, 
    		@RequestParam("name") String name) throws IOException{
    	
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, 1);
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
    	java.io.File cvf = convert(files);
        
    	File fileMetadata = new File();
    	fileMetadata.setParents(Collections.singletonList("1ObvVoBjBulSvs3wTDoTjCPhAyPUNRn5Y"))
    		.setName(name+"_"+format.format(cal.getTime())+".jpg");
    	FileContent mediaContent = new FileContent("image/jpeg", cvf);
    	File file = googleDrive.files().create(fileMetadata, mediaContent)
    	    .setFields("id")
    	    .execute();
    	return new ResponseEntity<Object>("https://drive.google.com/uc?export=view&id="+file.getId(), HttpStatus.OK);
    }
    @RequestMapping( value = "upload/CheckOut", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadCheckOut(@RequestParam("file") MultipartFile files, 
    		@RequestParam("name") String name) throws IOException{
    	
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, 1);
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
    	java.io.File cvf = convert(files);
        
    	File fileMetadata = new File();
    	fileMetadata.setParents(Collections.singletonList("11Gqvm6NMZ7SuxrSgb4iHvoKrPpFtKZNz"))
    		.setName(name+"_"+format.format(cal.getTime())+".jpg");
    	FileContent mediaContent = new FileContent("image/jpeg", cvf);
    	File file = googleDrive.files().create(fileMetadata, mediaContent)
    	    .setFields("id")
    	    .execute();
    	return new ResponseEntity<Object>("https://drive.google.com/uc?export=view&id="+file.getId(), HttpStatus.OK);
    }
    
    public java.io.File convert(MultipartFile file) throws IOException {
    	java.io.File convFile = new java.io.File(file.getOriginalFilename());
    	convFile.createNewFile();
    	FileOutputStream fos = new FileOutputStream(convFile);
    	fos.write(file.getBytes());
    	fos.close();
    	return convFile;
    }
}
