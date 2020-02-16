select
    member0_.member_id as col_0_0_,
    member0_.username as col_1_0_,
    member0_.age as col_2_0_,
    member0_.team_id as col_3_0_,
    team1_.name as col_4_0_
from
    member member0_
    cross join team team1_
where
    member0_.team_id = team1_.team_id
    and member0_.username = 'member4'
    and team1_.name = 'teamB'
    and member0_.age >= 35
    and member0_.age <= 40;