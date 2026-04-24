@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repo;

    public Customer save(Customer customer) {
        return repo.save(customer);
    }

    public List<Customer> getAll() {
        return repo.findAll();
    }
}