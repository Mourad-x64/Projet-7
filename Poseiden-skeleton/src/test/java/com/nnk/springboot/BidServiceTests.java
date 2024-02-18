package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;
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
public class BidServiceTests {

	@Autowired
	private BidListRepository bidListRepository;
	@Autowired
	private BidListService bidListService;

	@Before
	public void cleanDb(){
		bidListRepository.deleteAll();
	}

	@Test
	public void bidListTest() {
		BidList bid = new BidList();

		bid.setAccount("Account Test");
		bid.setType("Type Tes");
		bid.setBidQuantity(10d);

		// Save
		bid = bidListService.save(bid);
		Assert.assertNotNull(bid.getBidListId());
		Assert.assertEquals(bid.getBidQuantity(), 10d, 10d);

		// Update
		bid.setBidQuantity(20d);
		bid = bidListService.save(bid);
		Assert.assertEquals(bid.getBidQuantity(), 20d, 20d);

		// Find
		Integer id = bid.getBidListId();
		List<BidList> listResult = bidListService.findAll();
		Assert.assertTrue(listResult.size() > 0);
		Optional<BidList> bidList1 = bidListRepository.findById(id);
		BidList newBidlist;
		if (bidList1.isPresent()){
			newBidlist = bidList1.get();
			Assert.assertEquals(newBidlist.getBidListId(), id, id);
		}

		// Delete
		bidListService.deleteById(id);
		Optional<BidList> bidList2 = bidListRepository.findById(id);
		Assert.assertFalse(bidList2.isPresent());

	}
}
