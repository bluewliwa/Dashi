package api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.MySQLDBConnection;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

/**
 * Servlet implementation class VisitHistory
 */
@WebServlet("/history")
public class VisitHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final DBConnection connection = new MySQLDBConnection();

	// private static final DBConnection connection = new MongoDBConnection();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VisitHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());

		try {
			// JSONObject input = RpcParser.parseInput(request);

			HttpSession session = request.getSession();
			String userid = (String) session.getAttribute("user");
			if (userid == null) {
				response.setStatus(403);
				System.out.println("user not login!");
				return;
			} else {
				if (request.getParameterMap().containsKey("user_id")) {
					String userIdReq = request.getParameter("user_id");
					if (!userIdReq.equals(userid)) {
						System.out.println("user does not match!");
						if (session != null) {
							session.invalidate();
						}
						response.sendRedirect("index.html");
					}
				}
			}

			if (request.getParameterMap().containsKey("user_id")) {
				String userId = request.getParameter("user_id");
				// String userId = "1111";
				Set<String> bussinessIds = connection.getVisitedRestaurants(userId);
				if (bussinessIds.size() > 0) {
					List<JSONObject> list = new ArrayList<>();
					for (String busId : bussinessIds) {
						JSONObject responseDetailsJson = new JSONObject();
						responseDetailsJson.put("user_id", userId).put("bussiness_id", busId);
						list.add(responseDetailsJson);
					}
					RpcParser.writeOutput(response, new JSONArray(list));
				} else {
					RpcParser.writeOutput(response, new JSONObject().put("status", "Non visited history!"));
				}
			} else {
				RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);

		try {

			HttpSession session = request.getSession();
			String userid = (String) session.getAttribute("user");
			if (userid == null) {
				response.setStatus(403);
				System.out.println("user not login!");
				return;
			} else {
				if (request.getParameterMap().containsKey("user_id")) {
					String userIdReq = request.getParameter("user_id");
					if (!userIdReq.equals(userid)) {
						System.out.println("user does not match!");
						return;
					}
				}
			}

			JSONObject input = RpcParser.parseInput(request);
			if (input.has("user_id") && input.has("visited")) {
				String userId = (String) input.get("user_id");
				JSONArray array = (JSONArray) input.get("visited");
				List<String> visitedRestaurants = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					String businessId = (String) array.get(i);
					visitedRestaurants.add(businessId);
				}
				connection.setVisitedRestaurants(userId, visitedRestaurants);
				RpcParser.writeOutput(response, new JSONObject().put("status", "OK"));
			} else {
				RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);

		try {

			HttpSession session = request.getSession();
			String userid = (String) session.getAttribute("user");
			if (userid == null) {
				response.setStatus(403);
				return;
			} else {
				if (request.getParameterMap().containsKey("user_id")) {
					String userIdReq = request.getParameter("user_id");
					if (!userIdReq.equals(userid)) {
						return;
					}
				}
			}

			JSONObject input = RpcParser.parseInput(request);
			if (input.has("user_id") && input.has("visited")) {
				String userId = (String) input.get("user_id");
				JSONArray array = (JSONArray) input.get("visited");
				List<String> visitedRestaurants = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					String businessId = (String) array.get(i);
					visitedRestaurants.add(businessId);
				}
				connection.unsetVisitedRestaurants(userId, visitedRestaurants);
				RpcParser.writeOutput(response, new JSONObject().put("status", "OK"));
			} else {
				RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
