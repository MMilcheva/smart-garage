package com.example.smart_garage.controllers.mvc;

import com.example.smart_garage.dto.BrandFilterDto;
import com.example.smart_garage.dto.BrandResponse;
import com.example.smart_garage.dto.BrandSaveRequest;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.models.BrandFilterOptions;
import com.example.smart_garage.services.contracts.CarModelService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.example.smart_garage.exceptions.AuthenticationFailureException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.BrandMapper;
import com.example.smart_garage.models.Brand;
import com.example.smart_garage.models.User;
import com.example.smart_garage.services.contracts.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@Controller
@RequestMapping("/brands")
public class BrandMvcController {
    private final BrandService brandService;
    private final BrandMapper brandMapper;
    private final CarModelService carModelService;
    private final AuthenticationHelper authenticationHelper;

    public BrandMvcController(BrandService brandService,
                              BrandMapper brandMapper,
                              CarModelService carModelService, AuthenticationHelper authenticationHelper) {
        this.brandService = brandService;
        this.brandMapper = brandMapper;
        this.carModelService = carModelService;
        this.authenticationHelper = authenticationHelper;
    }


    @GetMapping("/{brandId}/car-models")
    public String getAllCarModelsByBrandId(@PathVariable Long brandId,  Model model, HttpSession session) {
//@ModelAttribute("carModelFilterOptions") CarModelFilterDto carModelFilterDto,
//
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        checkAccessPermissions(user);

        model.addAttribute("selectedCarModels", carModelService.getAllCarModelsByBrandId(brandId));
        return "SelectedCarModelsView";
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null; // Метода ще взима атрибутa с ключ CurrentUser и ще
        // връща отговор дали този атрибут изобщо съществува
    }

    @GetMapping("/{brandId}")
    public String showSingleBrand(@PathVariable Long brandId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Brand brand = brandService.getBrandById(brandId);
            model.addAttribute("brand", brand);
            model.addAttribute("selectedCarModels", carModelService.getAllCarModelsByBrandId(brandId));
            return "BrandView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping
    public String filterAllBrands(@ModelAttribute("brandFilterOptions") BrandFilterDto brandFilterDto,
                                  Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        String sortBy = brandFilterDto.getSortBy();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "brandName";
        }

        BrandFilterOptions brandFilterOptions = new BrandFilterOptions(
                brandFilterDto.getBrandName(),
                brandFilterDto.getArchived(),
                brandFilterDto.getSortBy(),
                brandFilterDto.getSortOrder());
        model.addAttribute("brands", brandService.getAllBrandsFilter(brandFilterOptions));
        return "BrandsView";
    }

    @GetMapping("/new")
    public String showNewBrandPage(Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("brandSaveRequest", new BrandSaveRequest());
        return "BrandCreateView";
    }

    @PostMapping("/new")
    public String createBrand(@Valid @ModelAttribute BrandSaveRequest brandSaveRequest,
                              BindingResult bindingResult,
                              Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "BrandCreateView";
        }

        try {
            Brand brand = brandMapper.convertToBrand(brandSaveRequest);
            Brand createdBrand = brandService.createBrand(brand);
            return "redirect:/brands/" + createdBrand.getBrandId();

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";

        }
    }

    @GetMapping("/{brandId}/update")
    public String showEditBrandPage(@PathVariable Long brandId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {

            Brand brand = brandService.getBrandById(brandId);
            BrandResponse brandResponse = brandMapper.convertToBrandResponse(brand);
            BrandSaveRequest brandSaveRequest = new BrandSaveRequest();
            brandSaveRequest.setBrandName(brandResponse.getBrandName());
            brandSaveRequest.setArchived(brandResponse.getIsArchived());
            model.addAttribute("brandId", brandId);
            model.addAttribute("brandSaveRequest", brandSaveRequest);
            return "BrandUpdateView";

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @PostMapping("/{brandId}/update")
    public String updateBrand(@PathVariable Long brandId,
                              @Valid @ModelAttribute("brandSaveRequest") BrandSaveRequest brandSaveRequest,
                              BindingResult bindingResult,
                              Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "BrandUpdateView";
        }
        try {
//            checkModifyPermissions(postId, user);
            Brand brand = brandMapper.convertToBrand(brandSaveRequest);
            brand.setBrandName(brandSaveRequest.getBrandName());
            brand.setArchived(brandSaveRequest.isArchived());
            Brand brandToBeUpdated = brandService.getBrandById(brandId);
            brandToBeUpdated.setBrandName(brand.getBrandName());
            brandToBeUpdated.setArchived(brand.isArchived());

            brandService.updateBrand(brandToBeUpdated);
            return "redirect:/brands/{brandId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";

        }

    }
    private static void checkAccessPermissions(User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}
