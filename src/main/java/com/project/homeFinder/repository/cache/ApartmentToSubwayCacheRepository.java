package com.project.homeFinder.repository.cache;

import com.project.homeFinder.domain.ApartmentToSubway;
import com.project.homeFinder.dto.cache.ApartmentToSubwayCacheDto;
import com.project.homeFinder.dto.cache.SerializeListOfApartmentToSubway;
import com.project.homeFinder.repository.ApartmentRepository;
import com.project.homeFinder.repository.SubwayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ApartmentToSubwayCacheRepository {

    private final RedisTemplate<String, SerializeListOfApartmentToSubway> apartmentToSubwayRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(1); // TTL = Time To Live

    private final ApartmentRepository apartmentRepository;
    private final SubwayRepository subwayRepository;

    public List<ApartmentToSubwayCacheDto> getApartmentToSubway(Long subwayId) {
        String key = getKey(subwayId);
        SerializeListOfApartmentToSubway serializeListOfApartmentToSubway = apartmentToSubwayRedisTemplate.opsForValue().get(key);
        if (serializeListOfApartmentToSubway == null) {
            return new ArrayList<>();
        }

        List<ApartmentToSubwayCacheDto> apartmentToSubway = apartmentToSubwayRedisTemplate.opsForValue().get(key).getData();

        log.info("Get ApartmentToSubway from Redis {},{}", key, apartmentToSubway);

        return apartmentToSubway;
    }

    public void setApartmentToSubway(List<ApartmentToSubwayCacheDto> apartmentToSubway) {
        Map<Long, List<ApartmentToSubwayCacheDto>> map = new HashMap<>();
        apartmentToSubway.stream().forEach(ats -> {
            Long key = ats.getSubwayId();
            List<ApartmentToSubwayCacheDto> value = map.getOrDefault(key, new ArrayList<>());
            value.add(ats);
            map.put(key, value);
        });

        for (Long k : map.keySet()) {
            List<ApartmentToSubwayCacheDto> value = map.get(k);
            String key = getKey(k);

            log.info("Set ApartmentToSubway to Redis {},{}", key, value);
            apartmentToSubwayRedisTemplate.opsForValue().set(key, SerializeListOfApartmentToSubway.of(value), USER_CACHE_TTL); // cache가 저장되고, USER_CACHE_TTL 이후에는 삭제됨
        }
    }

    private String getKey(Long subwayId) {
        return "SUBWAY:" + subwayId; //  단순히 username 으로만 key 값을 정의할 경우, 다른 데이터를 캐싱할때 username을 중복으로 key에서 사용 X & 어떤 데이터에 대한 케싱인지 알 수 없음 -> 앞에 prefix를 붙여줌
    }
}