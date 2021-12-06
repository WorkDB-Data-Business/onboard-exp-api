//package br.com.harvest.onboardexperience.infra.scorm;
//
//import br.com.harvest.onboardexperience.infra.scorm.ScormAPI;
//import com.rusticisoftware.cloud.v2.client.ApiException;
//import com.rusticisoftware.cloud.v2.client.Configuration;
//import com.rusticisoftware.cloud.v2.client.auth.HttpBasicAuth;
//import com.rusticisoftware.cloud.v2.client.model.CourseSchema;
//
//public class ScormTeste {
//
//
//    private final static String SCORM_ZIP_PATH = "D:\\Workspace\\WorkDB\\Harvest\\Testes\\sentimento.zip";
//
//    final static String COURSE_ID = "SENTIMENTO_COURSE";
//    final static String LEARNER_ID = "JOSEPH_LEARNER";
//    final static String REGISTRATION_ID = "SENTIMENTO_REGISTRO";
//
//    private final static String SECRET_KEY = "JWsfMbB7cbHi3tEMaw88a6Qy8Pebg31E9rdYBcpq";
//    private final static String APP_ID = "0LD3O1SVOV";
//
//    final static String OUTPUT_BORDER = "---------------------------------------------------------\n";
//
//    public static void main(String[] args) throws ApiException {
//
//        HttpBasicAuth auth = (HttpBasicAuth) Configuration.getDefaultApiClient().getAuthentication("APP_NORMAL");
//
//        auth.setUsername(APP_ID);
//        auth.setPassword(SECRET_KEY);
//
//        ScormAPI sc = new ScormAPI();
//
//        try
//        {
//
//
//            // Create a course and a registration
//            CourseSchema courseDetails = sc.createCourse(COURSE_ID, SCORM_ZIP_PATH);
////            sc.createRegistration(COURSE_ID, LEARNER_ID, REGISTRATION_ID);
//
//            System.out.println("Newly Imported Course Details: ");
//            System.out.println(courseDetails);
//
////            sc.deleteCourse(COURSE_ID);
////            sc.deleteRegistration(REGISTRATION_ID);
////
////
////            String launchLink = sc.buildLaunchLink(REGISTRATION_ID);
////
////            System.out.println(OUTPUT_BORDER);
////            System.out.printf("Launch Link: %s%n", launchLink);
////            System.out.println("Navigate to the url above to take the course. Hit enter once complete.");
////            try {
////                System.in.read();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////
////            RegistrationSchema registrationProgress = sc.getResultForRegistration(REGISTRATION_ID);
////
////            System.out.println(OUTPUT_BORDER);
////            System.out.println("Registration Progress: ");
////
////            List<CourseSchema> courseList = sc.getAllCourses();
////
////            System.out.println(OUTPUT_BORDER);
////            System.out.println("Course List: ");
////            for (CourseSchema course: courseList) {
////                System.out.println(course);
////            }
//
////            List<RegistrationSchema> registrationList = sc.getAllRegistrations();
////
////            System.out.println(OUTPUT_BORDER);
////            System.out.println("Registration List: ");
////            for (RegistrationSchema registration: registrationList)
////            {
////                System.out.println(registration);
////            }
//        } catch (ApiException | IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
