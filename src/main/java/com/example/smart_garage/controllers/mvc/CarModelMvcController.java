package com.example.smart_garage.controllers.mvc;


import com.example.smart_garage.dto.CarModelResponse;
import com.example.smart_garage.dto.CarModelSaveRequest;
import com.example.smart_garage.exceptions.AuthenticationFailureException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.dto.CarModelFilterDto;
import com.example.smart_garage.helpers.CarModelMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.BrandService;
import com.example.smart_garage.services.contracts.CarModelService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@Controller
@RequestMapping("/car-models")
public class CarModelMvcController {

    private final CarModelService carModelService;
    private final BrandService brandService;
    private final CarModelMapper carModelMapper;
    private final AuthenticationHelper authenticationHelper;

    public CarModelMvcController(CarModelService carModelService, BrandService brandService, CarModelMapper carModelMapper,
                                 AuthenticationHelper authenticationHelper) {
        this.carModelService = carModelService;
        this.brandService = brandService;
        this.carModelMapper = carModelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String filterAllCarModels(@ModelAttribute("carModelFilterOptions") CarModelFilterDto carModelFilterDto,
                                     Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);

        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        String sortBy = carModelFilterDto.getSortBy();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "carModelName";
        }
        CarModelFilterOptions carModelFilterOptions = new CarModelFilterOptions(
                carModelFilterDto.getCarModelId(),
                carModelFilterDto.getCarModelName(),
                carModelFilterDto.getBrandName(),
                carModelFilterDto.getArchived(),
                carModelFilterDto.getSortBy(),
                carModelFilterDto.getSortOrder());

        model.addAttribute("carModels", carModelService.getAllCarModelsFilter(carModelFilterOptions));

        return "CarModelsView";
    }

    @GetMapping("/{carModelId}")
    public String showSingleCarModel(@PathVariable Long carModelId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            CarModel carModel = carModelService.getCarModelById(carModelId);
            model.addAttribute("carModel", carModel);
            return "CarModelView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/new")
    public String showCarModelCreationForm(@ModelAttribute("carModelSaveRequest") CarModelSaveRequest carModelSaveRequest, Model model, HttpSession session, @RequestParam(required = false) Long brandId) {
        try {
            User user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
            CarModel carModel = new CarModel();

            if (brandId != null) {
                Brand brand = brandService.getBrandById(brandId);
                carModel.setBrand(brand);
            }


            model.addAttribute("carModel", carModel);
            model.addAttribute("brands", brandService.getAllBrandOptions());
            model.addAttribute("carModelSaveRequest", carModelSaveRequest);
            return "CarModelCreateView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";

        }
    }

    @PostMapping("/new")
    public String createCarModel(@ModelAttribute("carModelSaveRequest") CarModelSaveRequest carModelSaveRequest, BindingResult bindingResult, HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);

            if (bindingResult.hasErrors()) {
                return "CarModelCreateView";
            }


            CarModel carModel = carModelMapper.convertToCarModel(carModelSaveRequest);
            CarModel savedCarModel = carModelService.createCarModel(carModel);
            Long carModelId = savedCarModel.getCarModelId();
            return "redirect:/car-models/" + carModelId;

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";

        }
    }

    @GetMapping("/{carModelId}/update")
    public String showUpdateCarModelForm(@PathVariable Long carModelId, Model model, @RequestParam(required = false) Long brandId) {
        try {

            CarModel carModel = carModelService.getCarModelById(carModelId);
            Brand brand = carModel.getBrand();

            CarModelResponse carModelResponse = carModelMapper.convertToCarModelResponse(carModel);
            CarModelSaveRequest carModelSaveRequest = new CarModelSaveRequest();

            carModelSaveRequest.setCarModelName(carModelResponse.getCarModelName());
            carModelSaveRequest.setBrand(brand);
            carModelSaveRequest.setBrandId(brandId);
            carModelSaveRequest.setBrandName(carModelResponse.getBrandName());

            model.addAttribute("carModelId", carModelId);
            model.addAttribute("carModelSaveRequest", carModelSaveRequest);
            return "CarModelUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";

        }
    }

    @PostMapping("/{carModelId}/update")
    public String updateModel(@PathVariable Long carModelId,
                              @Valid @ModelAttribute("carModelSaveRequest") CarModelSaveRequest carModelSaveRequest,
                              BindingResult bindingResult,
                              Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);

        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "CarModelUpdateView";
        }
        try {
            CarModel carModelToBeUpdated = carModelService.getCarModelById(carModelId);
          carModelSaveRequest.setBrand(carModelToBeUpdated.getBrand());
            CarModel carModel = carModelMapper.convertToCarModel(carModelSaveRequest);

            carModelToBeUpdated.setCarModelName(carModel.getCarModelName());
            carModelToBeUpdated.setBrand(carModel.getBrand());
            carModelToBeUpdated.setCarModelId(carModelId);
            CarModel savedCarModel = carModelService.updateCarModel(carModelToBeUpdated);

            return "redirect:/car-models/" + carModelId;

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";

        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @GetMapping("/{carModelId}/delete")
    public String delete(@PathVariable Long carModelId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            carModelService.block(user, carModelId);
            return "redirect:/users";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView2";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    private static void checkAccessPermissions(User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }

}
