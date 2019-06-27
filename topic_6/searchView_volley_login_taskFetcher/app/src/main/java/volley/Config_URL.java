package volley;

//This class is for storing all URLs as a model of URLs

public class Config_URL
{
	// TODO: 6/22/2019 Replace base_URL with the IP address the server is running on.
	private static String base_URL = "http://192.168.31.106:5000/";
	public static String URL_LOGIN = base_URL+"todo/api/v1.0/users";
	public static String URL_REGISTER = base_URL+"android_login_api/";
	public static String URL_TASKS = base_URL+"todo/api/v1.0/tasks";
	public static String URL_TASK = base_URL+"todo/api/v1.0/tasks/";
}
