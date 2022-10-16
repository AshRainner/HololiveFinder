package com.anyholo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.anyholo.model.data.Kirinuki;
import com.anyholo.model.data.Member;
import com.anyholo.model.data.Twit;

public class DBController {
	private static String url = "jdbc:oracle:thin:@222.237.255.159:1521:xe";
	private static String userid = "HololiveFinder";
	private static String pwd ="8778";
	private static Connection con =null;
	private static PreparedStatement pstmt = null;
	public static void DBConnect() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, userid, pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void InitialValueInsert(Member m,String KRName) {
		DBConnect();
		String sql = "insert into member values(?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, KRName);
			pstmt.setString(2, m.getImageUrl());
			pstmt.setString(3, m.getCountry());
			pstmt.setString(4, m.getOnAir());
			pstmt.setString(5, m.getIntroduceText());
			pstmt.setString(6, m.getOnAirTitle());
			pstmt.setString(7, m.getOnAirthumnailsUrl());
			pstmt.setString(8, m.getChannelId());
			pstmt.setString(9, m.getTwitterUrl());
			pstmt.setString(10, m.getHololiveUrl());
			pstmt.setInt(11, m.getNumber());
			pstmt.setString(12, m.getOnAirVideoUrl());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBClose();
	}
	public static void DBClose() {
		try {
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int TwitSelect(String twid) {
		DBConnect();
		String sql = "select * from twit where twitid like ?";
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, twid);
			pstmt.executeQuery();
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	public static void TwitInsert(Twit t) {
		DBConnect();
		String sql = "insert into Twit values(?,?,?,?,?,?,?,?,?,TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'))";
		//TwitID, WriteUserName, UserID, UserProfileURL, TwitContent, TwitType, NextTwitID, MediaType, MediaURL, WriteDate
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, t.getTwitID());
			pstmt.setString(2, t.getWriteUserName());
			pstmt.setString(3, t.getUserID());
			pstmt.setString(4, t.getUserProfileURL());
			pstmt.setString(5, t.getTwitContent());
			pstmt.setString(6, t.getTwitType());
			pstmt.setString(7, t.getNextTwitID());
			pstmt.setString(8, t.getMediaType());
			pstmt.setString(9, t.getMediaURL());
			pstmt.setString(10, t.getWriteDate());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBClose();
	}
	public static void KirinukiInsert(Kirinuki k) {
		DBConnect();
		String sql = "insert into Kirinuki values(?,?,?,?,TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'))";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, k.getYoutubeURL());
			pstmt.setString(2, k.getChannelName());
			pstmt.setString(3, k.getThumnailsURL());
			pstmt.setString(4, k.getTag());
			pstmt.setString(5, k.getUploadTime());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBClose();
		
	}
	public static void DBUpdate(Member m) {
		DBConnect();
		String sql = "update member set ONAIR = ?, ONAIRTITLE = ?, ONAIRTHUMNAILSURL = ?, ONAIRVIDEOURL = ?"
				+ ", INTRODUCETEXT = ? where channelid like ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, m.getOnAir());
			pstmt.setString(2, m.getOnAirTitle());
			pstmt.setString(3, m.getOnAirthumnailsUrl());
			pstmt.setString(4, m.getOnAirVideoUrl());
			pstmt.setString(5, m.getIntroduceText());
			pstmt.setString(6, m.getChannelId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBClose();
	}
	public static void DBSelect(JSONArray jArray) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con =null;
			String sql = "SELECT * FROM member";
			con = DriverManager.getConnection(url, userid, pwd);
			if(con==null)
				System.out.println("시발");
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				JSONObject sObject = new JSONObject();
				sObject.put("memberName", rs.getString("memberName"));
				sObject.put("imageUrl", rs.getString("imageUrl"));
				sObject.put("country", rs.getString("country"));
				sObject.put("onAir", rs.getString("onair"));
				sObject.put("introduceText", rs.getString("introducetext"));
				sObject.put("onairTitle", rs.getString("onairtitle"));
				sObject.put("onAirThumnailsUrl", rs.getString("onairthumnailsurl"));
				sObject.put("onAirViedoUrl", rs.getString("onairvideourl"));
				sObject.put("channelId", rs.getString("channelid"));
				sObject.put("twitterurl", rs.getString("twitterurl"));
				sObject.put("hololiveUrl", rs.getString("hololiveurl"));
				sObject.put("num", rs.getInt("num"));
				jArray.add(sObject);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
