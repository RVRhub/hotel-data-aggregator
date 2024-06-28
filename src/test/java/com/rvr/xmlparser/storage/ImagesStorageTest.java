package com.rvr.xmlparser.storage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.rvr.xmlparser.utils.CleanUpImageStorageUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class ImagesStorageTest
{
	@Mock
	Resource imagesResourceDir;

	@Test
	void shouldSaveImageAndReturnItTest() throws IOException {
		String operationId = UUID.randomUUID().toString();

		File mockFile = mock(File.class);
		when(mockFile.getAbsolutePath()).thenReturn("./src/test/resources/images");
		when(imagesResourceDir.getFile()).thenReturn(mockFile);

		String filePath = "file:./src/test/resources/testImage.png";
		CleanUpImageStorageUtils.withCleanUpImageFolder(imagesResourceDir, () -> {
			ImagesStorage imagesStorage = new ImagesStorage(imagesResourceDir);
			imagesStorage.createImageOperationFolder(operationId);
			imagesStorage.save(filePath, operationId);

			List<String> images = imagesStorage.getImages(operationId);

			assertThat(images).isNotNull();
			assertThat(images.size()).isEqualTo(1);
		});
	}

	@Test
	void shouldNotReturnImagesWhenFolderIsEmptyTest() throws IOException {
		String operationId = UUID.randomUUID().toString();

		File mockFile = mock(File.class);
		when(mockFile.getAbsolutePath()).thenReturn("./src/test/resources/images");
		when(imagesResourceDir.getFile()).thenReturn(mockFile);

		ImagesStorage imagesStorage = new ImagesStorage(imagesResourceDir);
		List<String> images = imagesStorage.getImages(operationId);

		assertThat(images).isNotNull();
		assertThat(images.size()).isEqualTo(0);
	}
}
