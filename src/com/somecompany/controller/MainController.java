package com.somecompany.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.somecompany.controller.hr.EmployeeController;
import com.somecompany.controller.oe.CustomerController;
import com.somecompany.controller.oe.ManagerController;
import com.somecompany.repository.exceptions.DataAccessException;
import com.somecompany.service.hr.EmployeeService;
import com.somecompany.service.oe.CustomerService;
import com.somecompany.service.oe.OrderService;
import com.somecompany.service.oe.ProductService;
import com.somecompany.utils.DataSourceFactory;

/********************************************
 * MainController
 * 
 * 사용자의 입력을 받고 해당 컨트롤러에게 전달
 * 결과를 Request 객체에 바인딩
 * 설정된 view로 리다이렉팅/포워딩
*********************************************/

@SuppressWarnings("serial")
public class MainController extends HttpServlet {
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	// 상수
	
	private final static String MYBATIS_HR = "hr-mybatis-config.xml";
	private final static String MYBATIS_OE = "oe-mybatis-config.xml";
		
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	// 컨트롤러
	private Controller target;	// 컨트롤러 인터페이스 레퍼런스
	
	// EmployeeController(human_res)
	private EmployeeController empController = new EmployeeController();
	
	// CustomerController(cust)
	private CustomerController custController = new CustomerController();
	
	// ManagerController(manager)
	private ManagerController managerController = new ManagerController();
	
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	// 로거(SLF4j)
	
	private Logger logger;
	
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	// 초기화
	
	@Override
	public void init() throws ServletException {
		/********** Services **********/
		
		// EmployeeService
		EmployeeService empService = EmployeeService.getInstance(MYBATIS_HR);		
		
		// CustomerService
		CustomerService custService = CustomerService.getInstance(MYBATIS_OE);
				
		// ProductService
		ProductService prodService = ProductService.getInstance(
				DataSourceFactory.getOracleDataSource("oe", "oe"));
			
		// OrderService
		OrderService orderService = OrderService.getInstance(MYBATIS_OE);
		
		
		/********** Controllers **********/
		
		// EmployeeController(human_res)
		empController.setService(empService);
		
		// CustomerController(cust)		
		custController.setCustService(custService);
		custController.setProdService(prodService);
		custController.setOrderService(orderService);
		
		// ManagerController
		managerController.setOrderService(orderService);
		managerController.setProdService(prodService);
		managerController.setCustService(custService);
		
		/*** 로거 생성(slf4j) ***/
		logger = LoggerFactory.getLogger(MainController.class);
		
		super.init();
	}
	
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	// doGet & doPost: doSamba를 실행시킨다
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doSamba(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doSamba(req, resp);
	}
	
	////////////////////////////////////////////////////////////////
	// doSamba: doGet, doPost에서 공통적으로 실행
	
	private void doSamba(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String viewPage = doProcess(req, resp);	// 작업 처리(아래 내부 함수들에 의해)
		goView(req, resp, viewPage);			// 포워드 처리(리다이렉트/포워드)
	}
	
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	// common
	
	/* doProcess: doGet과 doPost에서 공통적으로 실시될 작업 담당 */
	private String doProcess(HttpServletRequest req, HttpServletResponse resp) {
		String viewPage = "/index.jsp";		// 디폴트 페이지
		Model model = new Model();
		
		try {
			req.setCharacterEncoding("utf-8");
			// 중간에 사용될 수 있는 request와 response 객체를 바인딩해준다
			model.addAttribute("request", req);
			model.addAttribute("response", resp);
			
			// 컨트롤러로 작업 위임
			viewPage = processBusiness(req, model);
			return viewPage;
			
		/* 예외 처리부 */
		} catch (NoSuchMethodException e) {
			logException(e, false);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			logException(e, false);
		} catch (InvocationTargetException e) {
			if(e.getCause() instanceof SQLException || e.getCause() instanceof PersistenceException) {
				viewPage = ("/errors/general_error.jsp?error_name=" + logException(e, true));
			} else {
				logException(e, true);
			}
		} catch (SecurityException e) {
			logException(e, false);
		} catch (DataAccessException | SQLException e) {
			viewPage = ("/errors/general_error.jsp?error_name=" + logException(e, true));
		} catch (NullPointerException e) {
			logException(e, true);
		} catch (ClassCastException e) {
			logException(e, true);
		} catch (IOException e) {
			logException(e, true);
		} catch (Exception e) {
			logException(e, true);
		}
		
		return viewPage;
	}
	
	
	/* forward 처리: url의 형식에 따라 리다이렉트나 포워드를 선택 처리 */	
	private void goView(HttpServletRequest req, 
			HttpServletResponse resp, String viewPage){
		
		if(viewPage.startsWith(":redirect")) {
			// viewPage의 url이 :redirect로 시작하면 리다이렉트 처리
			viewPage = viewPage.replace(":redirect", "");
			try {
				resp.sendRedirect(viewPage);
			} catch (IOException e) {
				logException(e, true);
			}
		} else {
			// 그 외에는 포워드 처리
			try {
				RequestDispatcher dispatcher = req.getRequestDispatcher(viewPage);
				dispatcher.forward(req, resp);
				
			} catch (ServletException | IOException e) {
				logException(e, true);
			}
		}
	}
	
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	// 내부 함수들
	
	/* processBusiness: 컨트롤러로 작업 위임 */
	private String processBusiness(HttpServletRequest req, Model model) 
			throws Exception {
		String viewPage = "/index.jsp";		// 디폴트 페이지
		
		// uri 파싱
		RequestCommandParam reqCommandParam = parseUri(req);
		
		// 컨트롤러에 전달될 파라미터 파싱
		Map<String, String> paramsMap = parseRequestMap(req, model);
		
		// 타겟 설정
		switch(reqCommandParam.mainPart) {
		case "human_res":	// human resources
			target = empController;
			break;
			
		case "cust":	// 고객용 페이지
			target = custController;
			break;
			
		case "manager":	// 관리자 페이지
			target = managerController;
			break;
			
		default:	// 부적절한 uri
			return "/index.jsp";		// 메인페이지로 보낸다
		}
		
		// 작업 처리
		viewPage = proceed(target, reqCommandParam, paramsMap, model);
		
		// 결과 바인딩
		bindingResultAttribute(target, req, reqCommandParam.requestAction, model);
		
		// 포워드할 페이지 리턴
		return viewPage;
	}
	
	
	/* proceed: 메소드 선택과 호출 */
	private String proceed(Controller controller, RequestCommandParam requestCommandParam, 
			Map<String, String> paramsMap, Model model) 
					throws NoSuchMethodException, SecurityException, 
					IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		// requestAction에 따라 메소드 선택
		Method method = getMethod(controller, requestCommandParam);
		
		// invoke를 통한 메소드 호출
		return (String) method.invoke(controller, paramsMap, model);
	}
	
	
	/* parseUri: URI를 파싱해서 커맨드 분석 */	
	private RequestCommandParam parseUri(HttpServletRequest req) {
		// 요청 파라미터 객체
		RequestCommandParam reqCommandParam = new RequestCommandParam();
		
		// 요청 URI
		String uri = req.getRequestURI();
		String[] parsedUri = uri.split("/");	// 구분자로 tokenizing
		String requestMethod = req.getMethod();
		
		// 파싱 작업
		for(int i = 1; i < parsedUri.length; i++) {
			if(i == 1) {
				// 2번 요소를 메인파트에 저장(1번 요소는 빈문자열)
				reqCommandParam.mainPart = parsedUri[i];
			} else if(i == parsedUri.length - 1) {
				// 마지막 요소를 요청 액션에 저장
				reqCommandParam.requestAction = parsedUri[i];
			} else {
				// 나머지는 서브 파트에 저장
				reqCommandParam.subPart.add(parsedUri[i]);
			}
		}
		
		reqCommandParam.requestMethod = requestMethod;
		
		return reqCommandParam;
	}
	
	
	/* parseRequestMap: 컨트롤러로 넘어갈 파라미터 파싱 */
	private Map<String, String> parseRequestMap(HttpServletRequest req, Model model) 
			throws IllegalStateException, IOException, ServletException {
		Map<String, String[]> reqParameterMap =  req.getParameterMap();
		String tempPath = "c:\\webtemp";
		
		// 파라미터 맵
		Map<String, String> paramsMap = new HashMap<String, String>();
		
		if(req.getContentType() != null 
				&& req.getContentType().toLowerCase().startsWith("multipart/")) {
			// multipart로 요청이 들어왔을 때(POST)			
			Collection<Part> parts = req.getParts();
			
			for(Part part : parts) {
				String contentType = part.getContentType();
				
				if(contentType == null) {
					// 일반 파라미터: 맵에 삽입
					paramsMap.put(part.getName(), readParameterValue(part));
				} else if(contentType.toLowerCase().startsWith("image/")) {
					long size = part.getSize();
					
					if(size > 0) {
						String fileName = getFileName(part);
						part.write(tempPath + "/" + fileName);
						paramsMap.put(part.getName(), fileName);
					}
				} else if(contentType.toLowerCase().startsWith("application/")) {
					// do nothing: 차후에 일반 파일을 이용할 경우 여기에 코딩할 것
				}
			}
			
		} else {
			// 파라미터 맵 파싱 작업(POST/GET, multipart 요청이 아님)
			for(String paramName : reqParameterMap.keySet())
				paramsMap.put(paramName, reqParameterMap.get(paramName)[0]);
		}
		
		return paramsMap;			
	}
	
	/* getFileName: 파일 처리를 위해(used in parseRequestMap) */
	/* 최범균 저 "JSP 2.2 웹프로그래밍" 참고  */
	private String getFileName(Part part) {
		for(String cd : part.getHeader("Content-Disposition").split(";")) {
			if(cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		
		return null;
	}
	
	/* readParameterValue: 파라미터 값 읽기(used in parseRequestMap) */
	/* 최범균 저 "JSP 2.2 웹프로그래밍" 참고  */
	private String readParameterValue(Part part) throws UnsupportedEncodingException, IOException {
		InputStreamReader reader = null;
		
		try {
			reader = new InputStreamReader(part.getInputStream(), "utf-8");
			char[] data = new char[512];
			int len = -1;
			
			StringBuilder builder = new StringBuilder();
			
			while((len = reader.read(data)) != -1) {
				builder.append(data, 0, len);
			}
			return builder.toString();
		} finally {
			reader.close();
		}
	}
	
	
	/* bindingResultAttribute1: 결과 바인딩하기 */
	private void bindingResultAttribute(
			Controller controller, HttpServletRequest req,
			String requestAction, Model model) {
		// 결과 바인딩을 위한 정보 객체를 가져온다(해당 컨트롤러가 소유하고 있음)
		ResultBindingType resultBindingType = controller.getResultBindingMap().get(requestAction);
		
		// ResultBindingType 객체가 존재한다면 작업을 실시한다
		if(resultBindingType != null) {
			String attrName = resultBindingType.attrName;
			Class<?> attrType = resultBindingType.attrType;
			
			bindingResultAttribute(req, attrName, model, attrType);
		} // 없다면 바인딩 작업을 하지 않는다
	}
	
	
	/* bindingResultAttribute2: 결과 바인딩하기 - 함수 1 내부에서 다시 호출됨 */
	@SuppressWarnings("unchecked")
	private <T> void bindingResultAttribute(HttpServletRequest req, 
			String attrName, Model model, Class<?> resultType) {
		// 작업 결과 수신
		Object resultObj =  model.getAttribute(attrName);
		T castedResultObj;
		
		// 작업 결과 바인딩
		if(resultObj != null && resultObj.getClass() == resultType) {
			
			castedResultObj = (T) resultObj;
			req.setAttribute(attrName, castedResultObj);
		}
	}	
	
	
	/* getMethod: requestAction에 따른 메소드 객체 가져오기 */
	private Method getMethod(Controller controller, RequestCommandParam requestCommandParam) 
			throws NoSuchMethodException, SecurityException {
		
		// 컨트롤러 클래스 메소드의 파라미터 타입(맵과 모델)
		Class<?>[] typeParams = {Map.class, Model.class };
		
		// 요청한 메소드 이름
		String requestProcessingMethodName = "";
		
		// 요청 방식에 따라 맵핑될 메소드 이름을 결정
		if(requestCommandParam.requestMethod.toUpperCase().equals("POST")) {
			// POST 방식
			requestProcessingMethodName = controller.getMethPostRequestMap().
					get(requestCommandParam.requestAction);
		} else if(requestCommandParam.requestMethod.toUpperCase().equals("GET")) {
			// GET 방식
			requestProcessingMethodName = controller.getMethGetRequestMap().
					get(requestCommandParam.requestAction);
		}
		
		// NullPointerException 발생 가능(requestProcessingMethodName에 해당하는 메소드가 없을 경우)
		Method method = controller.getClass().getMethod(requestProcessingMethodName, typeParams);
		
		return method;
	}
	
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	// utils 
	
	/* 예외 로그 출력 함수 */
	private String logException(Throwable e, boolean printStackTrace) {
		// 기본 정보 출력
		logger.info("Exception: " + e.getClass().getName() + ", " + e.getMessage());
		
		if(e.getCause() != null) {
			// 원인 예외가 있다면 재귀적으로 본 함수를 호출한다
			logException(e.getCause(), printStackTrace);
		}
		
		if(printStackTrace) {
			// 스택 트레이스 출력
			if(e.getStackTrace() != null) {
				int listSize = e.getStackTrace().length;
				
				if(listSize != 0) {					
					for(StackTraceElement stackList : e.getStackTrace()) {
						if(stackList != null) {
							if(!stackList.getClassName().startsWith("org.apache")) {
								// org.apache로 시작되는 클래스의 오류 정보는 제외한다(코드 정보만)
								logger.info("\t{}.{}:{}", stackList.getClassName(), 
										stackList.getMethodName(), stackList.getLineNumber());
							}
						}
					}
				} else {
					logger.info("\tempty stack trace");
				}
			} else {
				logger.info("\tnull statck trace");
			}
			
			// DB 관련 예외의 경우 부가 정보 출력
			if(e instanceof SQLException) {
				logger.info("\tSQLStatus: " + ((SQLException) e).getSQLState() + 
						", ErrorCode " + ((SQLException) e).getErrorCode());
			}
			
			if(e instanceof DataAccessException) {
				logger.info("\tSQLStatus: " + ((DataAccessException) e).getSQLState() + 
						", ErrorCode " + ((DataAccessException) e).getErrorCode());
			}
		}
		
		return e.getClass().getName();
	}
	
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	// 내부 클래스
	
	/* RequestCommandParam: 커맨드를 담고 있는 클래스 */
	// default(패키지) 접근 제한
	class RequestCommandParam {
		String mainPart = "";	// 요청 메인 파트(ex. human_res, products). 컨트롤러 선택을 위해
		
		// 서브 파트(ex. product, product/inventory)
		// 쓸 일은 없겠지만, 차후 사용하는 경우나 잘못된 요청을 처리하기 위해 둔다.
		List<String> subPart = new ArrayList<String>();	
		
		String requestAction = "";	// 요청 액션(ex. get, list 등)
		String requestMethod = "";	// 요청 방식(ex. GET, POST)
	}
}
