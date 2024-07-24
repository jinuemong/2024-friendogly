package com.woowacourse.friendogly.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.woowacourse.friendogly.auth.AuthArgumentResolver;
import com.woowacourse.friendogly.pet.controller.PetController;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.pet.dto.request.SavePetRequest;
import com.woowacourse.friendogly.pet.dto.response.FindPetResponse;
import com.woowacourse.friendogly.pet.dto.response.SavePetResponse;
import com.woowacourse.friendogly.pet.service.PetCommandService;
import com.woowacourse.friendogly.pet.service.PetQueryService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(PetController.class)
public class PetApiDocsTest extends RestDocsTest {

    @MockBean
    private PetCommandService petCommandService;

    @MockBean
    private PetQueryService petQueryService;

    @Autowired
    private AuthArgumentResolver authArgumentResolver;

    @DisplayName("반려견 등록 문서화")
    @Test
    void savePet_Success() throws Exception {
        Long loginMemberId = 1L;
        SavePetRequest requestDto = new SavePetRequest(
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                SizeType.SMALL.toString(),
                Gender.FEMALE.toString(),
                "https://google.com"
        );
        SavePetResponse response = new SavePetResponse(
                1L,
                loginMemberId,
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                SizeType.SMALL.toString(),
                Gender.FEMALE.toString(),
                "https://google.com"
        );
        MockMultipartFile image = new MockMultipartFile("image", "image", MediaType.MULTIPART_FORM_DATA.toString(), "asdf".getBytes());
        MockMultipartFile request = new MockMultipartFile("request", "request", "application/json", objectMapper.writeValueAsBytes(requestDto));

        Mockito.when(petCommandService.savePet(any(), any(), any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/pets")
                        .file(image)
                        .file(request)
                        .header(HttpHeaders.AUTHORIZATION, loginMemberId.toString()))
                .andExpect(status().isCreated())
                .andDo(MockMvcRestDocumentationWrapper.document("pet-save-201",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("image").description("강아지 프로필 이미지 파일"),
                                partWithName("request").description("강아지 등록 정보")
                        ),
                        requestPartFields(
                                "request",
                                fieldWithPath("name").type(JsonFieldType.STRING).description("반려견 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("반려견 한 줄 소개"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("반려견 생년월일: yyyy-MM-dd"),
                                fieldWithPath("sizeType").type(JsonFieldType.STRING).description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("반려견 이미지 URL")
                        ),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Pet API")
                                .summary("반려견 등록 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("로그인한 회원 id")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("반려견 id"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("반려견의 주인 id"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("반려견 이름"),
                                        fieldWithPath("data.description").type(JsonFieldType.STRING).description("반려견 한 줄 소개"),
                                        fieldWithPath("data.birthDate").type(JsonFieldType.STRING).description("반려견 생년월일: yyyy-MM-dd"),
                                        fieldWithPath("data.sizeType").type(JsonFieldType.STRING).description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                        fieldWithPath("data.gender").type(JsonFieldType.STRING).description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                        fieldWithPath("data.imageUrl").type(JsonFieldType.STRING).description("반려견 이미지 URL")
                                )
                                .requestSchema(Schema.schema("SavePetRequest"))
                                .responseSchema(Schema.schema("SavePetResponse"))
                                .build()))
                );
    }

    @DisplayName("반려견 단건 조회 문서화")
    @Test
    void findById_Success() throws Exception {
        Long petId = 1L;
        FindPetResponse response = new FindPetResponse(
                petId,
                1L,
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                SizeType.SMALL.toString(),
                Gender.FEMALE.toString(),
                "https://google.com"
        );

        Mockito.when(petQueryService.findById(any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/pets/{id}", petId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("pet-findById-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Pet API")
                                .summary("반려견 단건 조회 API")
                                .pathParameters(
                                        parameterWithName("id").description("조회하려는 반려견 id")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("반려견 id"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("반려견의 주인 id"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("반려견 이름"),
                                        fieldWithPath("data.description").type(JsonFieldType.STRING).description("반려견 한 줄 소개"),
                                        fieldWithPath("data.birthDate").type(JsonFieldType.STRING).description("반려견 생년월일: yyyy-MM-dd"),
                                        fieldWithPath("data.sizeType").type(JsonFieldType.STRING).description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                        fieldWithPath("data.gender").type(JsonFieldType.STRING).description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                        fieldWithPath("data.imageUrl").type(JsonFieldType.STRING).description("반려견 이미지 URL")
                                )
                                .requestSchema(Schema.schema("FindPetByIdRequest"))
                                .responseSchema(Schema.schema("FindPetByIdResponse"))
                                .build()))
                );
    }

    @DisplayName("내 반려견 목록 조회 문서화")
    @Test
    void findMine_Success() throws Exception {
        Long loginMemberId = 1L;
        FindPetResponse response1 = new FindPetResponse(
                1L,
                loginMemberId,
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                SizeType.SMALL.toString(),
                Gender.FEMALE.toString(),
                "https://google.com"
        );
        FindPetResponse response2 = new FindPetResponse(
                2L,
                loginMemberId,
                "뚱이",
                "뚱이입니다.",
                LocalDate.now().minusDays(2L),
                SizeType.LARGE.toString(),
                Gender.MALE_NEUTERED.toString(),
                "https://google.com"
        );
        List<FindPetResponse> response = List.of(response1, response2);

        Mockito.when(petQueryService.findByMemberId(any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/pets/mine")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, loginMemberId.toString()))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("pet-findMine-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Pet API")
                                .summary("내 반려견 목록 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("로그인한 회원 id")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("반려견 id"),
                                        fieldWithPath("data.[].memberId").type(JsonFieldType.NUMBER).description("반려견의 주인 id"),
                                        fieldWithPath("data.[].name").type(JsonFieldType.STRING).description("반려견 이름"),
                                        fieldWithPath("data.[].description").type(JsonFieldType.STRING).description("반려견 한 줄 소개"),
                                        fieldWithPath("data.[].birthDate").type(JsonFieldType.STRING).description("반려견 생년월일: yyyy-MM-dd"),
                                        fieldWithPath("data.[].sizeType").type(JsonFieldType.STRING).description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                        fieldWithPath("data.[].gender").type(JsonFieldType.STRING).description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                        fieldWithPath("data.[].imageUrl").type(JsonFieldType.STRING).description("반려견 이미지 URL")
                                )
                                .responseSchema(Schema.schema("FindPetResponse"))
                                .build()))
                );
    }

    @DisplayName("특정 회원의 반려견 목록 조회 문서화")
    @Test
    void findByMemberId_Success() throws Exception {
        Long memberId = 1L;
        FindPetResponse response1 = new FindPetResponse(
                1L,
                memberId,
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                SizeType.SMALL.toString(),
                Gender.FEMALE.toString(),
                "https://google.com"
        );
        FindPetResponse response2 = new FindPetResponse(
                2L,
                memberId,
                "뚱이",
                "뚱이입니다.",
                LocalDate.now().minusDays(2L),
                SizeType.LARGE.toString(),
                Gender.MALE_NEUTERED.toString(),
                "https://google.com"
        );
        List<FindPetResponse> response = List.of(response1, response2);

        Mockito.when(petQueryService.findByMemberId(any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/pets")
                        .param("memberId", memberId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("pet-findByMemberId-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Pet API")
                                .summary("특정 회원의 반려견 목록 조회 API")
                                .queryParameters(
                                        parameterWithName("memberId").description("조회하려는 회원의 id")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("반려견 id"),
                                        fieldWithPath("data.[].memberId").type(JsonFieldType.NUMBER).description("반려견의 주인 id"),
                                        fieldWithPath("data.[].name").type(JsonFieldType.STRING).description("반려견 이름"),
                                        fieldWithPath("data.[].description").type(JsonFieldType.STRING).description("반려견 한 줄 소개"),
                                        fieldWithPath("data.[].birthDate").type(JsonFieldType.STRING).description("반려견 생년월일: yyyy-MM-dd"),
                                        fieldWithPath("data.[].sizeType").type(JsonFieldType.STRING).description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                        fieldWithPath("data.[].gender").type(JsonFieldType.STRING).description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                        fieldWithPath("data.[].imageUrl").type(JsonFieldType.STRING).description("반려견 이미지 URL")
                                )
                                .responseSchema(Schema.schema("FindPetResponse"))
                                .build()))
                );
    }

    @Override
    protected Object controller() {
        return new PetController(petQueryService, petCommandService);
    }
}
