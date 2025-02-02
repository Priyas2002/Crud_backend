package com.ems.service;

import com.ems.entity.Employee;
import com.ems.entity.Employee.Status;
import com.ems.repository.EmployeeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(int id) {
        return employeeRepository.findById(id);
    }

    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }
    
//    public int getEmployeeCount() {
//    	return this.getAllEmployees().size();
//    }
    
    public long getEmployeeCount() {
    	return employeeRepository.count();
    }
    
// Get Employee Count by Status
    public int getEmployeeCountByStatus(Employee.Status status) {
        return employeeRepository.countEmployeesByStatus(status);
    }
    
//    public int getActiveEmployeeCount() {
//    	List<Employee> emps = this.getAllEmployees();
//    	int count=0;
//    	for(Employee e : emps) {
//    		
////    		System.out.println(e.getStatus());
//    		String sts = e.getStatus().toString();
//    		logger.info(sts);
//    		if(sts.equalsIgnoreCase("active")) {
//    			count++;
//    		}
//    	}
//    	return count;
//    }
}
