package org.combinator.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CombinatorNews {

     String newsTitle;
     String newsLink;
     String newsTotalComment;
     String newsScore;

}
