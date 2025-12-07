package com.rkumar.tikitly.services;

import com.rkumar.tikitly.dto.UserDto;
import com.rkumar.tikitly.exceptions.ResourceAlreadyExistsException;
import com.rkumar.tikitly.exceptions.ResourceNotFoundException;
import com.rkumar.tikitly.models.User;
import com.rkumar.tikitly.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if(user.isPresent()) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + userDto.getEmail());
        }

        User userToBeCreated = mapToEntity(userDto);
        User savedUser = userRepository.save(userToBeCreated);
        return mapToUserDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return mapToUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }


    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());

        User updatedUser = userRepository.save(user);
        return mapToUserDto(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }


    private User mapToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private UserDto mapToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }


}
