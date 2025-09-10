package com.insurance.policy.repo;

@DataJpaTest
class PolicyCustomerRelationTest {
    @Autowired CustomerRepository customerRepository;
    @Autowired PolicyRepository policyRepository;

    @Test
    @DisplayName("Test policy - customer relation")
    void testSavePolicyWithCustomer(){
        var customer = customerRepository.save(new Customer("Alice", "Johnson", "alice.johnosn@abc.com", "0400000000"));
        var policy = new Policy("ABC-12345",customer, 1500.00, LocalDate.now().plusDaya(1));

        var savedPolicy = policyRepository.save(policy);
        assertThat(savedPolicy.getID()).isNotNull();
        assertThat(savedPolicy.getCustomer().getEmail()).isEqualTo(customer.getEmail());
        assertThat(savedPolicy.getCreatedAt).isNotNull();
        assertThat(savedPolicy,getUpdatedAt).isNotNull();
    }
}