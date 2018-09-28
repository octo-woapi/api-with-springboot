package katapi.infrastructure.product.controller;

import katapi.KatapiApp;
import katapi.domain.product.Product;
import org.junit.Before;
import org.junit.Ignore;
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
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = KatapiApp.class)
@WebAppConfiguration
@EnableWebMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    /*****************************************
     *               GET
     *************************************** */

    @Test
    public void getAllProducts_shouldReturnAllProductsInDB() throws Exception {
        mockMvc.perform(get("/products")
                .accept(jsonType)
                .contentType(jsonType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON + ";charset=UTF-8"))
                .andExpect(content().contentType(jsonType))
                .andExpect(jsonPath("$", hasSize(12)))
        ;
    }

    @Test
    public void getAllProductsHypermedia_shouldReturnAllProductsInDBInHypermedia() throws Exception {
        mockMvc.perform(get("/products")
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(content().contentType(halJsonType))
                .andExpect(jsonPath("$._embedded.productResources", hasSize(12)))
        ;
    }

    @Test
    public void getProductById_shouldReturnTheCorrectProduct() throws Exception {
        int idToBeTested = 1;
        mockMvc.perform(get("/products/"+idToBeTested)
                .accept(jsonType)
                .contentType(jsonType))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json" + ";charset=UTF-8"))
                .andExpect(content().contentType(jsonType))
                .andExpect(jsonPath("$.product.id", is(idToBeTested)))
                ;
    }

    @Test
    public void getProductById_shouldReturn404ifNotFound() throws Exception {
        int idToBeTested = 999;
        mockMvc.perform(get("/products/"+idToBeTested)
                .accept(jsonType)
                .contentType(jsonType))
                .andExpect(status().is4xxClientError())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json" + ";charset=UTF-8"))
                .andExpect(content().contentType(jsonType))
                .andExpect(content().string(containsString("could not find product with id : "+idToBeTested)))
        ;
    }

    /*****************************************
     *               POST
     *************************************** */
    
    @Test
    public void createProduct_shouldReturnAProductWithID() throws Exception {
        String name = "tested";
        BigDecimal price = BigDecimal.valueOf(10.10);
        BigDecimal weight = BigDecimal.valueOf(12.34);
        String productJson = json(new Product(null, name, price, weight));
        mockMvc.perform(post("/products")
                .accept(jsonType)
                .contentType(jsonType)
                .content(productJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.price", is(price.doubleValue())))
                .andExpect(jsonPath("$.weight", is(weight.doubleValue())))
                .andExpect(jsonPath("$.id", is(notNullValue())))
        ;
    }

    @Test
    public void createProduct_shouldReturn400IfNameIsMissing() throws Exception{
        BigDecimal price = BigDecimal.valueOf(10.10);
        BigDecimal weight = BigDecimal.valueOf(12.34);
        String productJson = json(new Product(null, null, price, weight));
        mockMvc.perform(post("/products")
                .contentType(jsonType)
                .content(productJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Param 'name' is required")))
        ;
    }

    @Test
    public void createProduct_shouldReturn400IfPriceIsMissing() throws Exception{
        BigDecimal price = BigDecimal.valueOf(10.10);
        BigDecimal weight = BigDecimal.valueOf(12.34);
        String productJson = json(new Product(null, "tested", null, weight));
        mockMvc.perform(post("/products")
                .contentType(jsonType)
                .content(productJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Param 'price' is required")))
        ;
    }

    @Test
    public void createProduct_shouldReturn400IfWeightIsMissing() throws Exception{
        BigDecimal price = BigDecimal.valueOf(10.10);
        BigDecimal weight = BigDecimal.valueOf(12.34);
        String productJson = json(new Product(null, "tested", price, null));
        mockMvc.perform(post("/products")
                .contentType(jsonType)
                .content(productJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Param 'weight' is required")))
        ;
    }

    /*****************************************
     *               DELETE
     *************************************** */
    
    @Test
    public void deleteProduct_shouldReturn204() throws Exception {
        int idToBeTested = 2;
        mockMvc.perform(delete("/products/"+idToBeTested))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    public void deleteProduct_shouldReturn404IfProductNotFound() throws Exception{
        int idToBeTested = 5555;
        mockMvc.perform(delete("/products/"+idToBeTested))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("could not find product with id : "+idToBeTested)))
        ;
    }

    /*****************************************
     *               SORTING
     *************************************** */

    @Test
    public void getAllProducts_shouldSortByNameIfParamSortName() throws Exception{
        mockMvc.perform(get("/products/?sort=name")
                .accept(jsonType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(jsonType))
                .andExpect(jsonPath("$.[0].name", is("Cement bag 25kg")))
                .andExpect(jsonPath("$.[11].name", is("Screw box 75mm 50 units")))
        ;
    }

    @Test
    public void getAllProducts_shouldSortByWeightIfParamSortWeight() throws Exception{
        mockMvc.perform(get("/products/?sort=weight")
                .accept(jsonType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(jsonType))
                .andExpect(jsonPath("$.[0].weight", is(0.8)))
                .andExpect(jsonPath("$.[11].weight", is(855.0)))

        ;
    }

    @Test
    public void getAllProducts_shouldSortByPriceIfParamSortPrice() throws Exception{
        mockMvc.perform(get("/products/?sort=price")
                .accept(jsonType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(jsonType))
                .andExpect(jsonPath("$.[0].price", is(0.50)))
                .andExpect(jsonPath("$.[11].price", is(149.99)))
        ;
    }

    @Test
    public void getAllProducts_shouldIgnoreWrongSortParam() throws Exception{
        mockMvc.perform(get("/products/?sort=zezefzefzefzf")
                .accept(jsonType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(jsonType))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[1].id", is(2)))
                .andExpect(jsonPath("$.[11].id", is(12)))
        ;
    }

    @Test
    public void getAllProducts_shouldIgnoreEmptySortParam() throws Exception{
        mockMvc.perform(get("/products/?sort=")
                .accept(jsonType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(jsonType))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[11].id", is(12)))
        ;
    }

    /*****************************************
     *               PAGINATION
     *************************************** */

    @Test
    public void getAllProductsSorted_shouldReturnProductsFrom4to7IfRange4to7() throws Exception{
        mockMvc.perform(get("/products/?range=4-7")
                .accept(jsonType))
                .andExpect(status().isPartialContent())
                .andDo(print())
                .andExpect(content().contentType(jsonType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].id", is(5)))
                .andExpect(jsonPath("$.[2].id", is(7)))
        ;
    }

    @Test
    public void getAllProductsSorted_shouldStartPaginationAt0() throws Exception{
        mockMvc.perform(get("/products/?range=0-2")
                .accept(jsonType))
                .andExpect(status().isPartialContent())
                .andDo(print())
                .andExpect(content().contentType(jsonType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(1)))
        ;
    }

    @Test
    public void getAllProducts_shouldReturnProductsFrom5to10IfRange5to10() throws Exception{
        mockMvc.perform(get("/products/?range=5-10")
                .accept(jsonType))
                .andExpect(status().isPartialContent())
                .andDo(print())
                .andExpect(content().contentType(jsonType))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$.[0].id", is(6)))
                .andExpect(jsonPath("$.[4].id", is(10)))
        ;
    }

    @Test
    public void getAllProductsSorted_shouldHaveLinkContentRangeAcceptRangeHeaderWhenPaginating() throws Exception{
        mockMvc.perform(get("/products/?range=5-10"))
                .andDo(print())
                //.andExpect(header().string(HttpHeaders.LINK, "</products/?range=11-12>; rel=\"next\";"))
                .andExpect(header().string(HttpHeaders.CONTENT_RANGE, "5-10/12"))
                .andExpect(header().string(HttpHeaders.ACCEPT_RANGES, "product 5"))
        ;
    }

    @Test
    public void getAllProductsSorted_shouldlimitPaginationTo5elements() throws  Exception{
        mockMvc.perform(get("/products/?range=1-12"))
                .andExpect(jsonPath("$", hasSize(5)))
        ;
    }

    /* ****************************************************************************************************************
                                                    PRIVATE METHODS
    **************************************************************************************************************** */

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
