package smartparcel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiClient {

    private static final String API_BASE_URL = "http://localhost/smartparcel_api/";

    // Helper method to perform a POST request
    private static String doPost(String endpoint, Map<String, String> params) throws Exception {
        URL url = new URL(API_BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        StringJoiner sj = new StringJoiner("&");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);

        conn.setFixedLengthStreamingMode(out.length);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        conn.connect();

        try (OutputStream os = conn.getOutputStream()) {
            os.write(out);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        conn.disconnect();
        return response.toString();
    }
    
    // Helper method to perform a GET request
    private static String doGet(String endpoint) throws Exception {
         URL url = new URL(API_BASE_URL + endpoint);
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("GET");
         conn.setRequestProperty("Accept", "application/json");

         if (conn.getResponseCode() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
         }

         BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
         StringBuilder response = new StringBuilder();
         String output;
         while ((output = br.readLine()) != null) {
             response.append(output);
         }
         conn.disconnect();
         return response.toString();
    }


    // --- API Methods ---

    public static User login(String username, String password) {
        try {
            String jsonResponse = doPost("login.php", Map.of("username", username, "password", password));
            JSONObject responseObj = new JSONObject(jsonResponse);
            if (responseObj.getBoolean("success")) {
                JSONObject userObj = responseObj.getJSONObject("user");
                return new User(userObj.getInt("id"), userObj.getString("username"), userObj.getString("role"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Parcel getParcelStatus(String parcelId) {
        try {
            String jsonResponse = doGet("get_parcel.php?parcel_id=" + URLEncoder.encode(parcelId, "UTF-8"));
            JSONObject responseObj = new JSONObject(jsonResponse);
            if (responseObj.getBoolean("success")) {
                JSONObject parcelData = responseObj.getJSONObject("data");
                return new Parcel(
                    parcelData.getString("parcel_tracking_id"), parcelData.getString("sender_name"),
                    parcelData.getString("receiver_name"), parcelData.getString("pickup_point"),
                    parcelData.getString("drop_off_point"), parcelData.getString("status")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Parcel> getParcels(String statusFilter) {
        List<Parcel> parcelList = new ArrayList<>();
        try {
            String endpoint = "get_parcels.php";
            if (statusFilter != null && !statusFilter.isEmpty()) {
                endpoint += "?status=" + URLEncoder.encode(statusFilter, "UTF-8");
            }
            String jsonResponse = doGet(endpoint);
            JSONObject responseObj = new JSONObject(jsonResponse);
            if (responseObj.getBoolean("success")) {
                JSONArray parcelsArray = responseObj.getJSONArray("data");
                for (int i = 0; i < parcelsArray.length(); i++) {
                    JSONObject parcelData = parcelsArray.getJSONObject(i);
                    parcelList.add(new Parcel(
                        parcelData.getString("parcel_tracking_id"), parcelData.getString("sender_name"),
                        parcelData.getString("receiver_name"), parcelData.getString("pickup_point"),
                        parcelData.getString("drop_off_point"), parcelData.getString("status")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parcelList;
    }
    
    public static boolean createParcel(Map<String, String> parcelData) {
        try {
            String jsonResponse = doPost("create_parcel.php", parcelData);
            JSONObject responseObj = new JSONObject(jsonResponse);
            return responseObj.getBoolean("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateParcelStatus(String parcelId, String newStatus, int courierId) {
        try {
            // We convert the integer courierId to a String for the POST request.
            Map<String, String> params = Map.of(
                "parcel_id", parcelId,
                "status", newStatus,
                "courier_id", String.valueOf(courierId) // <-- NEW
            );

            String jsonResponse = doPost("update_parcel_status.php", params);
            JSONObject responseObj = new JSONObject(jsonResponse);
            return responseObj.getBoolean("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
