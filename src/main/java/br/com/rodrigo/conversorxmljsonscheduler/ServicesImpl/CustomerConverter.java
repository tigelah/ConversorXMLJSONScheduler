package br.com.rodrigo.conversorxmljsonscheduler.ServicesImpl;

import br.com.rodrigo.conversorxmljsonscheduler.domain.Customer;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomerConverter implements Converter {
    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        reader.moveDown();
        Customer customer = new Customer();
        customer.setId(Long.valueOf(reader.getValue()));

        reader.moveUp();
        reader.moveDown();
        customer.setFirstName(reader.getValue());

        reader.moveUp();
        reader.moveDown();
        customer.setLastName(reader.getValue());

        reader.moveUp();
        reader.moveDown();
        customer.setBirthdate(LocalDateTime.parse(reader.getValue(), DT_FORMATTER));

        return customer;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(Customer.class);
    }
}
