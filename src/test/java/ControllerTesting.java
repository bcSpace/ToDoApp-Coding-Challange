import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilderSupport;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import controller.AppController;
import service.EventLog;
import service.TodoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ControllerTesting {
	
	@Autowired
	private TodoService mockService;
	@Autowired
	private EventLog mockLog;
	
	@Autowired
	private AppController controller;
	
	
	private MockMvc mockMvc; 
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Configuration
    static class Config {
        @Bean
        public AppController getTodoController() {
        	return new AppController();
        }
 
        @Bean
        public TodoService getTodoService() {
            return new TodoService();
        }
        
        @Bean
        public EventLog getEventLog() {
        	return new EventLog();
        }
        
    }
	
	@After
	public void tearDown() {
		mockService.clearUsers();
	
	}
	
	
	@Test
	public void testUserManagement() throws Exception {
		//testing for using adding
		mockMvc.perform(MockMvcRequestBuilders.post("/addUser").param("username", "jeff")).andExpect(status().isOk());
		//testing for not adding dup users
		mockMvc.perform(MockMvcRequestBuilders.post("/addUser").param("username", "jeff")).andExpect(status().is(400));
		//checking the userList
		mockMvc.perform(MockMvcRequestBuilders.get("/userlist"))
		.andExpect(jsonPath("$.total").value("1"))
		.andExpect(jsonPath("$.users[0]").value("jeff"));
		//deleting users
		mockMvc.perform(MockMvcRequestBuilders.post("/deleteUser").param("username", "jeff"))
		.andExpect(jsonPath("$.response").value("User Deleted"));
		
	}
	
	@Test
	public void testTodo() throws Exception {
		mockService.addUser("chris");
		String endpoint = "/user/chris/";
		
		//testing the todoList, making sure it is clear
		mockMvc.perform(MockMvcRequestBuilders.get(endpoint+"todolist"))
		.andExpect(jsonPath("$.total").value("0"));
		
		//testing adding a todo item
		mockMvc.perform(MockMvcRequestBuilders.post(endpoint+"addTodo").param("title", "Test TODO").param("desc", "test desc").param("dueDate", "515118423"))
		.andExpect(status().is(201))
		.andExpect(jsonPath("$.title").value("Test TODO"));
		
		//re-testing todoList
		mockMvc.perform(MockMvcRequestBuilders.get(endpoint+"todolist"))
		.andExpect(jsonPath("$.total").value("1"));
		
		//testing details on one todo item
		mockMvc.perform(MockMvcRequestBuilders.get(endpoint+"todo/0"))
		.andExpect(jsonPath("$.title").value("Test TODO"))
		.andExpect(jsonPath("$.desc").value("test desc"))
		.andExpect(jsonPath("$.dueDate").value(515118423));
		
		//testing change 
		mockMvc.perform(MockMvcRequestBuilders.patch(endpoint+"todo/0")
		.param("title", "title change").param("desc", "").param("dueDate", ""))
		.andExpect(status().is(201));
		
		//checking the todo to make sure it changed
		mockMvc.perform(MockMvcRequestBuilders.get(endpoint+"todo/0"))
		.andExpect(jsonPath("$.title").value("title change"))
		.andExpect(jsonPath("$.desc").value("test desc")) //these should 
		.andExpect(jsonPath("$.dueDate").value(515118423)); //not change
		
		//testing delete
		mockMvc.perform(MockMvcRequestBuilders.post(endpoint+"deleteTodo").param("id", "0"))
		.andExpect(status().is(201))
		.andExpect(jsonPath("$.response").value("Deleted Todo"));
		
		//re-testing todoList
		mockMvc.perform(MockMvcRequestBuilders.get(endpoint+"todolist"))
		.andExpect(jsonPath("$.total").value("0"));
		
		
	}

}
