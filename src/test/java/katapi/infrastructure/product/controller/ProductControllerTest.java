package katapi.infrastructure.product.controller;

import katapi.KatapiApp;
import katapi.domain.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = KatapiApp.class)
@WebAppConfiguration
@EnableWebMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ProductControllerTest {

    /* ***************************************************************************************************************
                                                       INITIALISATION
     ************************************************************************************************************** */

    private MediaType jsonType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MediaType halJsonType = new MediaType("application", "hal+json", Charset.forName("UTF-8"));;

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }


    /* ***************************************************************************************************************
                                                            TESTS
     ************************************************************************************************************** */

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void listAllProducts_shouldReturnAllProductsInDB() throws Exception {
        mockMvc.perform(get("/products")
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(content().contentType(halJsonType))
                .andExpect(jsonPath("$.content", hasSize(7)))
                ;
    }

    @Test
    public void getProductById_shouldReturnTheCorrectProduct() throws Exception {
        int idToBeTested = 1;
        mockMvc.perform(get("/products/"+idToBeTested)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(content().contentType(halJsonType))
                .andExpect(jsonPath("$.product.id", is(idToBeTested)))
                ;
    }

    @Test
    public void getProductById_shouldReturn404ifNotFound() throws Exception {
        int idToBeTested = 999;
        mockMvc.perform(get("/products/"+idToBeTested)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaTypes.HAL_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(content().contentType(halJsonType))
                .andExpect(content().string(containsString("could not find product with id : "+idToBeTested)))
        ;
    }

    @Test
    public void createProduct_shouldReturnAProductWithID() throws Exception {
        String name = "tested";
        Double price = 10.10;
        Double weight = 12.34;
        String productJson = json(new Product(null, name, price, weight));
        mockMvc.perform(post("/products")
                .contentType(jsonType)
                .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.product.name", is(name)))
                .andExpect(jsonPath("$.product.price", is(price)))
                .andExpect(jsonPath("$.product.weight", is(weight)))
                .andExpect(jsonPath("$.product.id", is(notNullValue())))
        ;
    }

    @Test
    public void createProduct_shouldReturn400IfNameIsMissing() throws Exception{
        Double price = 10.10;
        Double weight = 12.34;
        String productJson = json(new Product(null, null, price, weight));
        mockMvc.perform(post("/products")
                .contentType(jsonType)
                .content(productJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Param 'name' is required")))
        ;
    }


    public void createProduct_shouldReturn201() throws Exception {
        mockMvc.perform(post("/products"));
    }



    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
