package com.joshir.els.api.controller;

import com.joshir.els.api.model.QueryRequest;
import com.joshir.els.api.model.QueryResponse;
import com.joshir.els.api.service.OrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value ="/documents",produces = "application/vnd.api.v1+json")
@Slf4j
public class DocumentController {
  private final OrderQueryService orderQueryService;
  public DocumentController(OrderQueryService orderQueryService) {
    this.orderQueryService = orderQueryService;
  }

  @Operation(summary="get all docs from orders index")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Success", content = {
                  @Content(mediaType = "application/vnd.api.v1+json", schema = @Schema( implementation = QueryResponse.class))
          }),
          @ApiResponse(responseCode = "500", description = "Internal Error."),
          @ApiResponse(responseCode = "400", description = "Not Found.")})
  @GetMapping
  @ResponseBody
  public ResponseEntity<List<QueryResponse>> getAllDocs() {
    List<QueryResponse> response = orderQueryService.getDocuments();
    log.info("returned {} documents",response.size());
    return ResponseEntity.ok(response);
  }

  @Operation(summary="get doc from orders index by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Success", content = {
                  @Content(mediaType = "application/vnd.api.v1+json", schema = @Schema( implementation = QueryResponse.class))
          }),
          @ApiResponse(responseCode = "500", description = "Internal Error."),
          @ApiResponse(responseCode = "400", description = "Not Found.")})
  @GetMapping("/{id}")
  @ResponseBody
  public ResponseEntity<QueryResponse> getDocumentById(@PathVariable @NotEmpty String id) {
    QueryResponse response = orderQueryService.getDocumentById(id);
    log.info("returned document {} for id: {}", response, id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary="get all docs from orders index")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Success", content = {
                  @Content(mediaType = "application/vnd.api.v1+json", schema = @Schema( implementation = QueryResponse.class))
          }),
          @ApiResponse(responseCode = "500", description = "Internal Error."),
          @ApiResponse(responseCode = "400", description = "Not Found.")})
  @GetMapping
  @PostMapping("/description")
  public @ResponseBody ResponseEntity<List<QueryResponse>> getAllDocsByDescription(@RequestBody @Valid QueryRequest request){
    List<QueryResponse>  response = orderQueryService.getDocumentByDescription(request.getDescription());
    log.info("returned {} documents",response.size());
    return ResponseEntity.ok(response);
  }
}
