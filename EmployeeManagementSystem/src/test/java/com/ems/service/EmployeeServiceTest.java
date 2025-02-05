package com.ems.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ems.entity.Employee;
import com.ems.entity.Employee.Status;
import com.ems.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	@Mock
	private EmployeeRepository employeeRepository;
	
	@InjectMocks
	private EmployeeService employeeService;
	
	@Test
	public void testSaveEmployee() {
		
		//Arrange
		Employee employee = new Employee();
		employee.setId(10);
		employee.setFirstName("Ayush");
		employee.setLastName("Das");
		
		when(employeeRepository.save(employee)).thenReturn(employee);
		
		//Act
		Employee created = employeeService.saveEmployee(employee);
		
		//Assert
		assertEquals("Ayush", created.getFirstName());
		verify(employeeRepository,times(1)).save(employee);
		
	}
	
	@Test
	public void testGetAllEmployees() {
		//Arrange
		Employee employee1 = new Employee();
		Employee employee2 = new Employee();
		
		employee2.setFirstName("Prakash");
		employee1.setId(11);
		employee1.setEmail("ps789@gmail.com");
		employee1.setPhoneNumber("7890334526");
		
		employee2.setFirstName("Priyas");
		employee2.setId(12);
		employee1.setEmail("ps345@gmail.com");
		employee1.setPhoneNumber("7890334774");
		
		List<Employee> list = List.of(employee1,employee2);
		when(this.employeeRepository.findAll()).thenReturn(list);
	    List<Employee> resultList=this.employeeService.getAllEmployees();
		assertEquals(2, resultList.size()); 
		assertEquals("Priyas",resultList.get(1).getFirstName());
	}
	
	@Test
    public void testGetEmployeeById() {
        // Arrange
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("John");

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        // Act
        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1);

        // Assert
        assertTrue(foundEmployee.isPresent());
        assertEquals("John", foundEmployee.get().getFirstName());
        verify(employeeRepository, times(1)).findById(1);
    }
	
	@Test
    public void testDeleteEmployee() {
        // Arrange
        int employeeId = 1;
        doNothing().when(employeeRepository).deleteById(employeeId);

        // Act
        employeeService.deleteEmployee(employeeId);

        // Assert
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
	
	@Test
    public void testGetEmployeeCount() {
        // Arrange
        when(employeeRepository.count()).thenReturn(5L);

        // Act
        long count = employeeService.getEmployeeCount();

        // Assert
        assertEquals(5, count);
        verify(employeeRepository, times(1)).count();
    }

	 @Test
	    public void testGetEmployeeCountByStatus() {
	        // Arrange
	        when(employeeRepository.countEmployeesByStatus(Status.ACTIVE)).thenReturn(3);

	        // Act
	        int count = employeeService.getEmployeeCountByStatus(Status.ACTIVE);

	        // Assert
	        assertEquals(3, count);
	        verify(employeeRepository, times(1)).countEmployeesByStatus(Status.ACTIVE);
	    }
	
	 @Test
	    public void testGetEmployeesByStatus() {
	        // Arrange
	        Employee employee1 = new Employee();
	        Employee employee2 = new Employee();
	        employee1.setStatus(Status.ACTIVE);
	        employee2.setStatus(Status.ACTIVE);

	        List<Employee> activeEmployees = List.of(employee1, employee2);
	        when(employeeRepository.findByStatus(Status.ACTIVE)).thenReturn(activeEmployees);

	        // Act
	        List<Employee> result = employeeService.getEmployeesByStatus(Status.ACTIVE);

	        // Assert
	        assertEquals(2, result.size());
	        verify(employeeRepository, times(1)).findByStatus(Status.ACTIVE);
	    }
}
