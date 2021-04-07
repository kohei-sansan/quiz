package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import connection.ConnectionGetter;
import dao.QuestionsDao;
import dao.UserDao;
import entity.Question;
import entity.User;

public class Test {
	public static void main(String[] args) throws Exception{
		Calendar cal = Calendar.getInstance();
		
		switch(cal.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			System.out.println("sunday");
		case Calendar.MONDAY:
			System.out.println("monday");
		case Calendar.TUESDAY:
			System.out.println("tuesday");
		case Calendar.WEDNESDAY:
			System.out.println("wednesday");
		case Calendar.THURSDAY:
			System.out.println("thursday");
		case Calendar.FRIDAY:
			System.out.println("friday");
		case Calendar.SATURDAY:
			System.out.println("saturday");
		}
	}
}
