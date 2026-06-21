package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.category.CreateCategoryOutput;
import github.com.diegogrlima.gocoffe.application.dto.category.GetAllCategoryOutput;
import github.com.diegogrlima.gocoffe.domain.category.usecase.CreateCategoryUseCase;
import github.com.diegogrlima.gocoffe.domain.category.usecase.DeleteCategoryByIdUseCase;
import github.com.diegogrlima.gocoffe.domain.category.usecase.GetAllCategoryUseCase;
import github.com.diegogrlima.gocoffe.domain.category.usecase.UpdateCategoryByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CreateCategoryUseCase createCategoryUseCase;

    @Mock
    private GetAllCategoryUseCase getAllCategoryUseCase;

    @Mock
    private UpdateCategoryByIdUseCase updateCategoryByIdUseCase;

    @Mock
    private DeleteCategoryByIdUseCase deleteCategoryByIdUseCase;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void shouldReturn201WhenCreateCategory() throws Exception {
        String name = "Bebidas";
        UUID categoryId = UUID.randomUUID();

        CreateCategoryOutput output = new CreateCategoryOutput(categoryId, name);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(output);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + name + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(categoryId.toString()))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    void shouldReturn200WhenFindAllCategories() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        GetAllCategoryOutput category1 = new GetAllCategoryOutput(id1, "Bebidas");
        GetAllCategoryOutput category2 = new GetAllCategoryOutput(id2, "Sobremesas");

        when(getAllCategoryUseCase.execute())
                .thenReturn(List.of(category1, category2));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id1.toString()))
                .andExpect(jsonPath("$[0].name").value("Bebidas"))
                .andExpect(jsonPath("$[1].id").value(id2.toString()))
                .andExpect(jsonPath("$[1].name").value("Sobremesas"));

        verify(getAllCategoryUseCase).execute();
    }

    @Test
    void shouldReturn204WhenUpdateCategory() throws Exception {
        UUID categoryId = UUID.randomUUID();
        String updatedName = "Bebidas Atualizado";

        doNothing().when(updateCategoryByIdUseCase).execute(any());

        mockMvc.perform(put("/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + updatedName + "\"}"))
                .andExpect(status().isNoContent());

        verify(updateCategoryByIdUseCase).execute(any());
    }

    @Test
    void shouldReturn204WhenDeleteCategory() throws Exception {
        UUID categoryId = UUID.randomUUID();

        doNothing().when(deleteCategoryByIdUseCase).execute(any());

        mockMvc.perform(delete("/categories/" + categoryId))
                .andExpect(status().isNoContent());

        verify(deleteCategoryByIdUseCase).execute(categoryId);
    }
}
