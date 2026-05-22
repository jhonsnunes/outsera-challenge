package com.challenge.outsera;

import com.challenge.outsera.application.usecase.CsvImportMovieUseCase;
import com.challenge.outsera.application.usecase.dto.AwardProducerResponse;
import com.challenge.outsera.application.usecase.dto.AwardResponse;
import com.challenge.outsera.infrastructure.movie.persistence.MovieRepository;
import com.challenge.outsera.infrastructure.producer.persistence.ProducerRepository;
import com.challenge.outsera.infrastructure.studio.persistence.StudioRepository;
import com.challenge.outsera.support.MovielistProposalExpectation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AwardIntervalIntegrationTest {

    private static final String MOVIELIST_CLASSPATH = "movielist.csv";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CsvImportMovieUseCase csvImportMovieUseCase;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private StudioRepository studioRepository;

    @BeforeEach
    void setUp() {
        clearDatabase();
        csvImportMovieUseCase.importMovies();
    }

    @Test
    void awardProposalFileShouldBeUnchanged() throws Exception {
        ClassPathResource resource = new ClassPathResource(MOVIELIST_CLASSPATH);

        try (InputStream inputStream = resource.getInputStream()) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }

            String checksum = HexFormat.of().formatHex(digest.digest());

            assertThat(checksum)
                    .as("movielist.csv foi alterado; atualize MovielistProposalExpectation se a mudança for intencional")
                    .isEqualTo(MovielistProposalExpectation.CSV_SHA256);
        }
    }

    @Test
    void awardIntervalEndpointShouldReturnDataFromProposalFile() throws Exception {
        String json = mockMvc.perform(get("/award-interval").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AwardResponse response = objectMapper.readValue(json, AwardResponse.class);

        assertProducerEntries(response.min(), MovielistProposalExpectation.MIN);
        assertProducerEntries(response.max(), MovielistProposalExpectation.MAX);
    }

    private void assertProducerEntries(List<AwardProducerResponse> actual, List<AwardProducerResponse> expected) {
        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expected);
    }

    private void clearDatabase() {
        movieRepository.deleteAll();
        producerRepository.deleteAll();
        studioRepository.deleteAll();
    }
}
