package com.nnk.springboot;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class CurvePointServiceTests {

	@Autowired
	private CurvePointRepository curvePointRepository;
	@Autowired
	private CurvePointService curvePointService;

	@Before
	public void cleanDb(){
		curvePointRepository.deleteAll();
	}

	@Test
	public void curvePointTest() {
		CurvePoint curvePoint = new CurvePoint();


		curvePoint.setTerm(10d);
		curvePoint.setValue(30d);

		// Save
		curvePoint = curvePointService.save(curvePoint);
		Assert.assertNotNull(curvePoint.getCurvePointId());


		// Update
		curvePoint.setTerm(40d);
		curvePoint = curvePointService.save(curvePoint);
		Assert.assertEquals(curvePoint.getTerm(), 40d, 40d);

		// Find
		List<CurvePoint> listResult = curvePointService.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = curvePoint.getCurvePointId();
		curvePointService.deleteById(id);
		Optional<CurvePoint> curvePointList = curvePointService.findById(id);
		Assert.assertFalse(curvePointList.isPresent());
	}

}
