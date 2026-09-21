package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.AbstractIntegrationTest;
import io.github.luviuche.hotel.entity.*;
import io.github.luviuche.hotel.enums.ActivationStatus;
import io.github.luviuche.hotel.enums.RoomStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Regression: derived queries over foreign keys must resolve the nested
 * traversal (property.id, for instance) and NOT be read as a scalar attribute
 * that does not exist. The getXxxId() helpers on the entities made Spring Data
 * generate invalid JPQL; the underscore (findByProperty_Id) prevents that.
 */
class ForeignKeyDerivedQueriesTest extends AbstractIntegrationTest {

    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private HotelChainRepository hotelChainRepository;
    @Autowired private PropertyRepository propertyRepository;
    @Autowired private RoomTypeRepository roomTypeRepository;
    @Autowired private RoomRepository roomRepository;

    @Test
    void findByRole_IdResolvesTheTraversal() {
        Role role = new Role();
        role.setName("ROLE_FK_TEST");
        role = roleRepository.save(role);

        User user = new User();
        user.setRole(role);
        user.setName("Ana");
        user.setLastName("Perez");
        user.setEmail("ana.fk@mail.com");
        user.setPassword("x");
        user.setPhone("300");
        user.setDocumentNumber("CC-FK-1");
        userRepository.save(user);

        assertThat(userRepository.findByRole_Id(role.getId()))
                .extracting(User::getEmail)
                .containsExactly("ana.fk@mail.com");
    }

    @Test
    void existsByProperty_IdAndNumberResolvesTheTraversal() {
        HotelChain chain = new HotelChain();
        chain.setName("ACM_FK");
        chain.setStatus(ActivationStatus.ACTIVE);
        chain = hotelChainRepository.save(chain);

        Property property = new Property();
        property.setHotelChain(chain);
        property.setName("Downtown");
        property.setAddress("Main street 1");
        property.setCity("Bogota");
        property.setPhone("300");
        property.setEmail("downtown@mail.com");
        property.setCategory("4");
        property.setStatus(ActivationStatus.ACTIVE);
        property = propertyRepository.save(property);

        RoomType roomType = new RoomType();
        roomType.setName("TYPE_FK_TEST");
        roomType = roomTypeRepository.save(roomType);

        Room room = new Room();
        room.setProperty(property);
        room.setRoomType(roomType);
        room.setNumber("101");
        room.setFloor(1);
        room.setCapacity(2);
        room.setPricePerNight(new BigDecimal("120.00"));
        room.setStatus(RoomStatus.AVAILABLE);
        room.setAvailable(true);
        roomRepository.save(room);

        assertThat(roomRepository.existsByProperty_IdAndNumber(property.getId(), "101")).isTrue();
        assertThat(roomRepository.existsByProperty_IdAndNumber(property.getId(), "999")).isFalse();
        assertThat(roomRepository.findByProperty_Id(property.getId())).hasSize(1);
    }
}
