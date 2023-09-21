package com.cst438.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Modify Enrollment repository interface for new query method findEnrollmentByStudentId
public interface EnrollmentRepository extends CrudRepository <Enrollment, Integer> {

	@Query("select e from Enrollment e where e.student.email=:email and e.year=:year and e.semester=:semester")
	public List<Enrollment> findStudentSchedule(
			@Param("email") String email,
			@Param("year") int year,
			@Param("semester") String semester);

	@Query("select e from Enrollment e where e.student.email=:email and e.course.course_id=:course_id")
	Enrollment findByEmailAndCourseId(@Param("email") String email, @Param("course_id") int course_id);

	@Query("select e from Enrollment e where e.student.student_id = :studentId")
	List<Enrollment> findEnrollmentByStudentId(@Param("studentId") int studentId);
}