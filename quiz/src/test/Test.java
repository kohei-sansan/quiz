package test;

import java.nio.file.Path;

import java.nio.file.Paths;
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
		Path path = Paths.get(".");
		String url = path.toAbsolutePath().toString();
		
		System.out.println(url);
	}
}
