package com.ems.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ems.entity.Employee;
import com.ems.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ✅ Handle Employees Page and Fetch Manager Info from Session
    @GetMapping("/employees")
    public String listEmployees(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            model.addAttribute("managerFirstName", session.getAttribute("managerFirstName"));
            model.addAttribute("managerLastName", session.getAttribute("managerLastName"));
            model.addAttribute("managerEmail", session.getAttribute("managerEmail"));
        }

        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("totalEmployees", employees.size());
        model.addAttribute("employees", employees);

        model.addAttribute("activeEmployeeCount", employeeService.getEmployeeCountByStatus(Employee.Status.ACTIVE));
        model.addAttribute("inactiveEmployeeCount", employeeService.getEmployeeCountByStatus(Employee.Status.INACTIVE));
        model.addAttribute("onLeaveEmployeeCount", employeeService.getEmployeeCountByStatus(Employee.Status.ON_LEAVE));

        return "manager/employees";
    }

    // ✅ Handle Dashboard Page and Fetch Manager Info from Session
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
        	 model.addAttribute("managerFirstName", session.getAttribute("managerFirstName"));
             model.addAttribute("managerLastName", session.getAttribute("managerLastName"));
            model.addAttribute("managerEmail", session.getAttribute("managerEmail"));
        }

        model.addAttribute("totalEmployees", employeeService.getEmployeeCount());
        model.addAttribute("activeEmployees", employeeService.getEmployeeCountByStatus(Employee.Status.ACTIVE));
        model.addAttribute("inactiveEmployees", employeeService.getEmployeeCountByStatus(Employee.Status.INACTIVE));
        model.addAttribute("onLeaveEmployees", employeeService.getEmployeeCountByStatus(Employee.Status.ON_LEAVE));

        return "manager/dashboard";
    }

	@GetMapping("/employees/add")
	public String showAddEmployeeForm(Model model) {
		model.addAttribute("employee", new Employee());
		return "manager/addEmployee";
	}

	@PostMapping("/employees/add")
	public String addEmployee(@RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName,
			@RequestParam("email") String email, @RequestParam("phone_no") String phoneNumber,
			@RequestParam("position") String position, @RequestParam("status") String status,
			@RequestParam("joined_date") String joinedDate) {

		Employee employee = new Employee();
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setEmail(email);
		employee.setPhoneNumber(phoneNumber);
		employee.setPosition(Employee.Position.valueOf(position));
		employee.setStatus(Employee.Status.valueOf(status));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		employee.setJoinedDate(LocalDate.parse(joinedDate, formatter));

		employeeService.saveEmployee(employee);
		return "redirect:/manager/employees";
	}

	@GetMapping("/update/{id}")
	public String showUpdateEmployeeForm(@PathVariable int id, Model model) {
		Employee employee = employeeService.getEmployeeById(id).orElse(null);
		if (employee == null) {
			return "redirect:/manager/employees";
		}
		model.addAttribute("employee", employee);
		return "manager/updateEmployee"; // This will load updateEmployee.html
	}

	@PostMapping("/employees/update")
	public String updateEmployee(@RequestParam("id") int id, @RequestParam("first_name") String firstName,
			@RequestParam("last_name") String lastName, @RequestParam("email") String email,
			@RequestParam("phone_no") String phoneNumber, @RequestParam("position") String position,
			@RequestParam("status") String status, @RequestParam("joined_date") String joinedDate) {

		// ✅ Fix: Get Employee Properly
		Employee employee = employeeService.getEmployeeById(id).orElse(null);

		if (employee != null) {
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			employee.setEmail(email);
			employee.setPhoneNumber(phoneNumber);
			employee.setPosition(Employee.Position.valueOf(position));
			employee.setStatus(Employee.Status.valueOf(status));

			// ✅ Fix: Format Joined Date Correctly
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			employee.setJoinedDate(LocalDate.parse(joinedDate, formatter));

			employeeService.saveEmployee(employee); // ✅ Updates Employee in Database
		}

		return "redirect:/manager/employees"; // ✅ Redirects to Employees Page
	}

	@GetMapping("/employees/delete/{id}")
	public String deleteEmployee(@PathVariable int id) {
		employeeService.deleteEmployee(id);
		return "redirect:/manager/employees";
	}
	
	@GetMapping("/employees/status/{status}")
	public String listEmployeesByStatus(@PathVariable String status, Model model) {
	    List<Employee> filteredEmployees;

	    if ("ALL".equalsIgnoreCase(status)) {
	        filteredEmployees = employeeService.getAllEmployees();
	    } else {
	        filteredEmployees = employeeService.getEmployeesByStatus(Employee.Status.valueOf(status.toUpperCase()));
	    }

	    model.addAttribute("employees", filteredEmployees);
	    model.addAttribute("selectedStatus", status);
	    return "manager/employees"; // Reload the employees.html with filtered data
	}

}
